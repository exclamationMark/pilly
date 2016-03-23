from flask import Flask
import json
from datetime import datetime, timedelta
import time, threading
app = Flask(__name__)

fileName = 'pbdata.json'
pbs = []

def saveFile():
	global pbs
	with open(fileName, 'w') as outfile:
		json.dump(pbs, outfile)

def readFile():
	global pbs
	with open(fileName, 'r') as infile:
		pbs = json.load(infile)

def getLastEntry(pb):
	newest = datetime.fromtimestamp(0)
	for h in pb['history']:
		t = datetime.strptime(h['date'], "%Y-%m-%d %H:%M:%S")
		if t > newest:
			newest = t
			hate = h
	return hate

def scanHistory(p):
	dump = []
	for h in p['history']:
		t = datetime.strptime(h['date'], "%Y-%m-%d %H:%M:%S")
		entry = [];
		entry.append(int(time.mktime(t.timetuple())))
		entry.append(float(h['weight']))
		entry.append(int(round(float(h['weight']) / float(p['pillweight']))))
		dump.append( entry )
	events = sorted(dump)
	return events

def getPillBox(id):
	for p in pbs:
		if p['id'] == int(id):
			return p
	return False

def getTimeToNextPill(id):
	p = getPillBox(id)
	getLastEntry(p)
	pillCount = round(float(h['weight']) / float(p['pillweight']))



@app.route('/')
def hello_world():
    return 'Hello World! this works locally!'

@app.route('/about')
def about():
    return 'This is the pilly server backend!'

@app.route('/report/<pid>/<weight>')
def report(pid, weight):
	p = getPillBox(pid)
	if p == False:
		return "pillbox not found!"
	event = {}
	event['date'] = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
	event['weight'] = weight
	p['history'].append(event)
	return "ok!"

@app.route('/status/<pid>')
def status(pid):
	p = getPillBox(pid)
	if p==False:
		return 'pilly not found!'
	h = getLastEntry(p)
	r = {}
	r['id'] = p['id']
	r['payload'] = h['weight']
	r['lastUpdate'] = h['date']
	r['pillcount'] = round( float(h['weight']) / float(p['pillweight']) )
	return json.dumps(r)

@app.route('/history/<pid>')
def history(pid):
	p = getPillBox(pid)
	if p==False:
		return 'no match!'
	events = scanHistory(p)
	return str(events)

@app.route('/historyRaw/<pid>')
def historyRaw(pid):
	p = getPillBox(pid)
	if p==False:
		return 'no match!'
	return str(p['history'])

@app.route('/dump')
def dump():
	global pbs
	return str(pbs)

@app.route('/dump2')
def dump2():
	global pbs
	s = ""
	for p in pbs:
		s += str(p['id'])
		s += '<br />'
	return s

@app.route('/save')
def save():
	saveFile()
	return 'Simulation state SAVED'

@app.errorhandler(404)
def page_not_found(e):
    """Return a custom 404 error."""
    return 'Sorry, nothing at this URL.', 404

readFile()
if __name__ == '__main__':
    app.run('10.132.0.2', 5000, True)]