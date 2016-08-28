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
	
	pid = 0 # Maybe not necessary
	description = "Dad's heart pills"
	pillCount = 0 # Also maybe not necessary
	pillWeight = 1
	history = []

	def __init__(self, pid):
		self.pid = pid
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
		response['recent'] = [['4 hours ago', 'taken on time'],['yesterday 20:14', 'taken on time'],['yestarday 10:38', 'taken 2h late'],['2 days ago 20:08', 'taken on time'],['2 days ago 08:02', 'taken on time']]
		return response

	def notifyCaretaker(self):
		return ""

	def setPillCount(self, pillCount):
		diff = pillCount - self.pillCount
		self.pillCount = pillCount
		if diff != 0:
			event = {}
			event["time"] = time.time()
			event["pillDelta"] = diff
			self.history.append(event)
		self.saveFile()

@app.route('/status/<pid>')
def status(pid):
	p = Pilly.get(pid)
	return json.dumps(p.status())

@app.route('/updatePillCount/<pid>/<pillCount>')
def updatePillCount(pid, pillCount):
	p = Pilly.get(pid)
	p.setPillCount(int(pillCount))
	return json.dumps({'response':'ok'})

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
