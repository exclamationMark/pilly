from flask import Flask
from flask import render_template, redirect, request, url_for
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

@app.route('/login')
def login():
	return render_template('login.html')

@app.route('/about')
def about():
	return 'You reached the About page! Congratulations! Now go back <a href="/">home</a>'

if __name__=='__main__':
	app.run(host='0.0.0.0')