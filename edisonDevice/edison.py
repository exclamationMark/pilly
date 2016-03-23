import mraa
import time, threading

treshold_change = 5
treshold_confirm = 2


pot = mraa.Aio(0)
pot.setBit(12)


def readSensor():
	suum = 0
	for i in range(1000):
		suum += float(pot.read())
	readout = suum/ 1000
	return readout

lastReadout = 0

def monitor():
	global lastReadout
	readout = readSensor()
	if abs(readout - lastReadout) > treshold_change:
		print "change detected!, veryfing value"
		confirmed = True
		for i in range(5):
			time.sleep(0.4)
			veryfingReadout = readSensor()
			if abs(veryfingReadout - readout) > treshold_confirm:
				confirmed = False
				break
		if confirmed:
			reportNewValue(readout)
			lastReadout = readout
		else:
			print "change ignored"

	#call itself in 5 sec
	threading.Timer(1, monitor).start()

def reportNewValue(value):
	print "new value: " + str(value)
	#and you know, upload to server and shit, though I ain't got time for that nao

monitor()
