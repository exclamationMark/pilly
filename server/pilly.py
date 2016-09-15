from flask import Flask
from flask import render_template, redirect, request, url_for
import json
from datetime import datetime, timedelta
import time, threading
app = Flask(__name__)

pillies = {}

configFileName = 'serverConfig.json'
class Pilly(object):
	global pillies

	def __init__(self, pid):
		self.pid = pid
		self.description = "Dad's heart pills"
		self.pillWeight = 1
		self.pillCount = 0
		self.history = []
		event = {}
		event["time"] = long(time.time())
		event["pillDelta"] = 0
		event["pillCount"] = 0
		event["minutesFromSchedule"] = "N/D"
		self.history.append(event)
		self.readFile()
		pillies[pid] = self

	@staticmethod
	def get(pid):
		if pid not in pillies:
			pillies[pid] = Pilly(pid)
		return pillies[pid]


	def readFile(self):
		fileName = str(self.pid) + '.json'
		try:
			with open(config['saveFilePath'] + fileName, 'r') as infile:
				fileData = json.load(infile)
				self.pillCount = fileData['pillCount']
				self.pillWeight = fileData['pillWeight']
				self.description = fileData['description']
				self.history = fileData['history']
		except:
			print "no file! creating new pilly!"
			#self.comments = []

	def saveFile(self):
		fileName = str(self.pid) + '.json'
		with open(config['saveFilePath'] + fileName, 'w') as outfile:
			json.dump(self.__dict__, outfile)

	def status(self):
		response = {}
		response['description'] = self.description
		response['pillCount'] = self.pillCount
		response['status'] = "ok"
		response['recent'] = self.history[-5:]
		return response

	def notifyCaretaker(self):
		return ""

	def setPillCount(self, pillCount):
		diff = pillCount - self.pillCount
		self.pillCount = pillCount
		if diff != 0:
			event = {}
			event["time"] = long(time.time())
			event["pillDelta"] = diff
			event["pillCount"] = self.pillCount
			event["minutesFromSchedule"] = "N/D"
			self.history.append(event)
		self.saveFile()

	def getRecentEvents(self, eventCount):
		return self.history[-int(eventCount):]

	def getRecentUnchecked(self):
		if self.history[-1]["minutesFromSchedule"] != "N/D":
			return []
		i = 1
		while self.history[-i]["minutesFromSchedule"] == "N/D" and i < len(self.history):
			i += 1
		return self.history[-i+1:]

	def setEventChecked(self, eventId, minutesFromSchedule):
		self.history[-eventId]["minutesFromSchedule"] = minutesFromSchedule
		self.saveFile()

@app.route('/')
def home():
	return json.dumps({'onFire' : 'False'})

@app.route('/status/<pid>')
def status(pid):
	p = Pilly.get(pid)
	return json.dumps(p.status())

@app.route('/updatePillCount/<pid>/<pillCount>')
def updatePillCount(pid, pillCount):
	p = Pilly.get(pid)
	p.setPillCount(int(pillCount))
	return json.dumps({'response':'ok'})

@app.route('/getRecentEvents/<pid>/<eventCount>')
def retrieveEvents(pid, eventCount):
	p = Pilly.get(pid)
	response = p.getRecentEvents(eventCount)
	return json.dumps(response)

@app.route('/getRecentUnchecked/<pid>')
def retrieveUnchecked(pid):
	p = Pilly.get(pid)
	response = p.getRecentUnchecked()
	return json.dumps(response)

@app.route('/setEventChecked/<pid>/<eventId>/<minutesFromSchedule>')
def setEventChecked(pid, eventId, minutesFromSchedule):
	p = Pilly.get(pid)
	p.setEventChecked(int(eventId), int(minutesFromSchedule))
	return json.dumps({'response' : 'ok'})

@app.route('/getPillWeight')
def getPillWeight():
	return "6.7"

@app.route('/candyCounter')
def candyCounter():
	return render_template('candy_count.html')

if __name__ == "__main__":
	try:
		with open (configFileName, 'r') as configFile:
			config = json.load(configFile)
	except IOError:
		print "Config file not found! Loading defaults"
		config = {}
		config['ip'] = '127.0.0.1'
		config['port'] = 5000
		config['debug'] = True

	app.run(config['ip'], config['port'], config['debug'])
