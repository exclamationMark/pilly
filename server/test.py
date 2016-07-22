from flask import Flask
from flask import render_template, redirect, request, url_for, request
import time
app = Flask(__name__)

events = [['2016-03-23 12:00', 'Pill taken - On time'],['2016-03-23 12:00', 'Pill taken - On time'],['2016-03-23 12:00', 'Pill taken - On time'],['2016-03-23 12:00', 'Pill taken - On time'],['2016-03-23 12:00', 'Pill taken - On time']]
hoursleft = 2
pillcount = 5

@app.route('/')
def index():
	return render_template('index.html')

@app.route('/control_panel')
def control_panel():
	return render_template('control_panel.html', events = events, hoursleft = hoursleft, pillcount = pillcount)

@app.route('/login', methods = ['POST', 'GET'])
def login():
	if request.method == 'POST':
		print request.form['username']
		print request.form['password']
		try:
			r = request.form['remember']
			print 'Checked'
		except KeyError:
			print 'Unchecked'
		return redirect(url_for('control_panel'))
	else:
		return render_template('login.html')

@app.route('/about')
def about():
	return 'You reached the About page! Congratulations! Now go back <a href="/">home</a>'

@app.route('/getinfo/123')
def getinfo():
	time.sleep(2)
	return '{"description" : "Dad\'s heart medication", "pillCount" : 6, "nextPillTime" : 72, "recent" : [{"date" : "2000-12-8 21:05", "event" : "On time"}, {"date" : "2000-12-9 21:02", "event" : "On time"}, {"date" : "2000-12-10 22:07", "event" : "1h late"}, {"date" : "2000-12-11 21:08", "event" : "On time"}, {"date" : "2000-12-12 20:58", "event" : "On time"}]}'

if __name__=='__main__':
	app.run(host='0.0.0.0')