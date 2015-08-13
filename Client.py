#!/usr/bin/env python

"""
Constructed using the pygame.joystick library: https://www.pygame.org/docs/ref/joystick.html
'map' function from: https://mail.python.org/pipermail/tutor/2013-August/097291.html

Game Controller Button Map
0 - Square
1 - Cross/X
2 - Circle/O
3 - Triangle
4 - L1
5 - R1
6 - L2
7 - R2
8 - SELECT
9 - START
10 - L Joystick Press
11 - R Joystick Press
12 - Home/PS
"""

import socket
import pygame
from time import sleep

TCP_IP = '10.82.78.50' # Set to Raspi IP
TCP_PORT = 5005
BUFFER_SIZE = 1024
MESSAGE = "System online"

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((TCP_IP, TCP_PORT))
#s.send(MESSAGE)

def stop():
	pygame.quit()
	exit()

buttonNames = {0 : "Square",
			 1 : "Cross/X",
			 2 : "Circle/O",
			 3 : "Triangle",
			 4 : "L1",
			 5 : "R1",
			 6 : "L2",
			 7 : "R2",
			 8 : "Select",
			 9 : "Start",
			 10 : "R Joystick",
			 11 : "L Joystick",
			 12 : "Home/PS"
			 }

#Map functions to the corresponding indices here
buttonMap = ["", #Square
                 "", #Cross/X
                 "", #Circle/O
                 "", #Triangle
                 "", #L1
                 "", #R1
                 "", #L2
                 "", #R2
                 "", #Select
                 "", #Start
                 "", #Right Joystick
                 "", #Left Joystick
                 stop  #Home/PS
                 ]

# Pygame Inititalisation
pygame.init()
pygame.joystick.init()
joystick = pygame.joystick.Joystick(0)

joystick.init() #Initialize joystick for use

servoPos = [0,0,0]
#Map inputs in order to gain a better comparison and control scheme
def map(x, in_min, in_max, out_min, out_max):
	return (x - in_min) * (out_max - out_min) // (in_max - in_min) + out_min

def throttleSteeringToLeftRight(inThrottle, inSteering):
	left = min(100, max(-100, inThrottle - inSteering)); 
	right = min(100, max(-100, inThrottle + inSteering)); 
	return [left, right]

def checkButtons():
	for button in range(joystick.get_numbuttons()):
		buttonState = joystick.get_button(button)
		if buttonState == 1 :
			print str(buttonNames[button]) + " has been pressed (" + str(button) + ")"
			try:
				buttonMap[button]() #Call any functions listed with the button in buttonMap
			except:
				pass #Button is not assigned

while True:
	for event in pygame.event.get(): #Update controller positions
		if event.type == pygame.JOYBUTTONDOWN:
			#print "Button Press detected"
			checkButtons()

	#Movement Controls (Left, Right)
	if joystick.get_button(11) == 1 :
		inputMap = [map(joystick.get_axis(2), -1, 1, -100, 100), map(joystick.get_axis(3), -1, 1, -100, 100)]

	else:
		inputMap = [map(joystick.get_axis(2), -1, 1, -75, 75), map(joystick.get_axis(3), -1, 1, -75, 75)]



	turn = throttleSteeringToLeftRight(inputMap[0], inputMap[1])
	print turn

	s.send(str(turn)) #Send output of throttleSterringToLeftRight as a list for the server to interpret
	sleep(0.1) #Debounce controls  
"""
        Not in use
	#if joystick.get_button(4) == 1:
		#s.send("bot.spinAtPcSpeed(1,75)")
		#s.send(1,75)

	#if joystick.get_button(5) == 1:
		#s.send("bot.spinAtPcSpeed(2,-75)")
		#s.send(2,-75)

	#if joystick.get_button(6) == 1:
		#s.send("bot.spinAtPcSpeed(4,75)")
		#s.send(3,-75)

	#if joystick.get_button(7) == 1:
		#s.send("bot.spinAtPcSpeed(3,-75)")
		#s.send(4,75)
        
	#print "Bot_LR: " + str(Bot_LR)
	#print "Bot_FB: " + str(Bot_FB)

	turn = throttleSteeringToLeftRight(inputMap[0], inputMap[1])
	print turn

	s.send(str(turn)) #Send output of throttleSterringToLeftRight as a list for the server to interpret


        Not in use
        #s.send("bot.spinAtPcSpeed(1,turn[0])")
	#s.send("bot.spinAtPcSpeed(2,turn[1])")
	#s.send("bot.spinAtPcSpeed(3,turn[1])")
	#s.send("bot.spinAtPcSpeed(4,turn[0])")

	#Camera Controls LR FB H
	#servoCommands = [joystick.get_axis(1), joystick.get_axis(1),joystick.get_hat(0)[1] ]
	#bot.moveToDegAngle(5, servoPos[0] + servoCommands[0], 50)
	#bot.moveToDegAngle(6, servoPos[1] + servoCommands[1], 50)
	#bot.moveToDegAngle(7, servoPos[2] + servoCommands[2], 50)

	#Update servo positions
	#servoPos[0] += servoCommands[0]
	#servoPos[1] += servoCommands[1]
	#servoPos[2] += servoCommands[2]

        

	sleep(0.1) #Debounce controls  
"""





#s.close()
