from flask import Flask
from flask import render_template, redirect, request, url_for
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
	for h in p['history'][-6:]:
		t = datetime.strptime(h['date'], "%Y-%m-%d %H:%M:%S")
		entry = [];
		entry.append(str(int(   (datetime.now() - t).total_seconds() / 60)) + ' minutes ago')
		entry.append(float(h['count']))
		dump.append( entry )
	events = dump[::-1]
	return events

def getPillBox(id):
	for p in pbs:
		if p['id'] == int(id):
			return p
	return False

def getTimeToNextPill(id):
	p = getPillBox(id)
	getLastEntry(p)



@app.route('/')
def hello_world():
    return render_template('index.html')

@app.route('/about')
def about():
    return 'This is the pilly server backend!'

@app.route('/login')
def login():
	return render_template('login.html')

@app.route('/report/<pid>/<hours>/<count>')
def report(pid, count, hours):
	p = getPillBox(pid)
	if p == False:
		return "pillbox not found!"
	event = {}
	event['date'] = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
	event['count'] = count
	p['history'].append(event)
	p['hoursleft'] = hours
	return "ok!"

@app.route('/status/<pid>')
def status(pid):
	p = getPillBox(pid)
	if p==False:
		return 'pilly not found!'
	h = getLastEntry(p)
	r = {}
	r['id'] = p['id']
	r['pillCount'] = h['count']
	r['lastUpdate'] = h['date']
	return json.dumps(r)

@app.route('/pilly/<pid>')
def control_panel(pid):
	p = getPillBox(pid)
	if p==False:
		return 'no match!'
	events = scanHistory(p)
	h = getLastEntry(p)
	hoursleft = p['hoursleft']
	return render_template('control_panel.html', events = events[0:5], hoursleft = int(hoursleft), pillcount = h['count'])

@app.route('/history/<pid>')
def history(pid):
	p = getPillBox(pid)
	if p==False:
		return 'no match!'
	events = scanHistory(p)
	#detect only pill value changes
	
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
    app.run('10.132.0.2', 5000, True)