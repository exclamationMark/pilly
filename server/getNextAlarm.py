import subprocess
import time

if __name__ == '__main__':
	log_out = subprocess.check_output("adb shell dumpsys alarm")
	lines = log_out.split("\n")
	found = False
	for line in lines:
		if "pilly" in line:
			words = line.split(" ")
			when = words[words.index("when") + 1]
			millis = long(when)
			seconds = millis/1000
			nt = time.localtime(seconds)
			print " ",nt.tm_mday, "/", nt.tm_mon, "/", nt.tm_year, " ", nt.tm_hour,":",nt.tm_min,":",nt.tm_sec
			found = True
			break
		if "ast-due" in line:
			break
	if not found:
		print "  Alarm not set"
