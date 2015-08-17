import serial
import sys


import time



# important AX-12 constants
AX_WRITE_DATA = 3
AX_READ_DATA = 4

s = serial.Serial()			   # create a serial port object
s.baudrate = 57600			  # baud rate, in bits/second
s.port = "/dev/ttyAMA0"		   # this is whatever port your are using
s.timeout = 3.0
s.open()


DXL_REG_CCW_Angle_Limit = 8 #to change control mode
DXL_REG_Goal_Postion = 30
DXL_REG_Moving_Speed = 32

#Servo IDs
servoIDs = {"Wheel 1" : 1, "Wheel 2" : 2, "Wheel 3" : 3, "Wheel 4" : 4, "Shoulder" : 5, "Tilt" : 6, "Pan" : 7}

def writeShort(val):
	s.write(chr(int(val)%256))
	s.write(chr(int(val)>>8))

def writeWord(ID, addr, val):
	s.write('W'+'w'+chr(ID))
	writeShort(addr)
	writeShort(val)

def jointMode(ID):
	s.write('W'+'j'+chr(ID))

def setPosition(ID, pos, vel):
	s.write('W'+'p'+chr(ID))
	writeShort(pos)
	writeShort(vel)




def moveToDxAngle(ID,dxlPosition,dxlSpeed):
	setPosition(ID,dxlPosition,dxlSpeed)

def moveToDegAngle(ID, degPosition, pcSpeed):
	while degPosition > 180:
		degPosition = degPosition - 360
	while degPosition < -180:
		degPosition = degPosition + 360

	if (degPosition < -150):
		degPosition = -150
	if (degPosition > 150):
		degPosition = 150
	moveToDxAngle(ID, int(float(degPosition)*3.41+511.5), int(float(pcSpeed)*10.23))

def spinAtDxSpeed(ID,dxlSpeed):
	writeWord(ID,DXL_REG_Moving_Speed,dxlSpeed)

# Spins at a certain percent of full speed.
def spinAtPcSpeed(ID,pcSpeed):
	if pcSpeed >= 0:
		spinAtDxSpeed(ID,int(float(pcSpeed)*10.23))
	else:
		spinAtDxSpeed(ID,1024+int(float(-pcSpeed)*10.23))

def throttleSteeringToLeftRight(inThrottle, inSteering):
	left = min(100, max(-100, inThrottle - inSteering));
	right = min(100, max(-100, inThrottle + inSteering));
	return (left, right)



# Purge the first value
time.sleep(0.5)
#print("Press [triangle] to exit.")
shoulderPos = -45
tiltPos = 0
panPos = 90

# Set wheel and joint modes.
writeWord(1, DXL_REG_CCW_Angle_Limit, 0)
writeWord(2, DXL_REG_CCW_Angle_Limit, 0)
writeWord(3, DXL_REG_CCW_Angle_Limit, 0)
writeWord(4, DXL_REG_CCW_Angle_Limit, 0)

jointMode(5)
jointMode(6)
jointMode(7)


tiltSpeed = 100
panSpeed = 100
shoulderSpeed = 20

# Shoulder is limited to -90 and 150. Note that this will hit the ground (which could be desired).
shoulderPos = max(-90, min(150, shoulderPos))


tiltcmd = tiltPos + shoulderPos
pancmd = panPos

# Tilt is limited to 90 degrees, pan to 150.
tiltcmd = max(-90, min(90, tiltcmd))
pancmd = max(-150, min(150, pancmd))

moveToDegAngle(5, shoulderPos,shoulderSpeed)
moveToDegAngle(6, tiltcmd, max(10, tiltSpeed))
moveToDegAngle(7, pancmd, max(10, panSpeed))

time.sleep(2.05)

print("Server Ready")
