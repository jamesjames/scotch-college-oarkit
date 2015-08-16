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

TCP_IP = '192.168.100.1'
TCP_PORT = 5005
BUFFER_SIZE = 1024
MESSAGE = "System online"

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((TCP_IP, TCP_PORT))
#s.send(MESSAGE)


#Pygame stuff
pygame.init()
#background_colour = (255,255,255)

#screen = pygame.display.set_mode((500, 400), 0, 32)
#pygame.display.set_caption('Controller Status')
#screen.fill(background_colour)

#pygame.display.flip()
# set up fonts
#basicFont = pygame.font.SysFont(None, 48)

# set up the text
#text = basicFont.render('Hello world!', True, (255,255,0), (255,0,0))


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

hatState = []
buttonState = []

pygame.init()
pygame.joystick.init()
joystick = pygame.joystick.Joystick(0)

joystick.init() #Initialize joystick for use

servoPos = [0,0,0]

def map(x, in_min, in_max, out_min, out_max):
        return x

for button in range(0,12):
        buttonState.append([button + 1, 0])


while True:
        for event in pygame.event.get():
                if event.type == pygame.QUIT:
                        stop()


        #Send positions of all axes [Right stick Vertical, Right Stick Horizontal, Left Stick Vertical, Left Stick Horizontal, Hat Vertical, Hat Horizontal]
        positions = [joystick.get_axis(0), joystick.get_axis(1), joystick.get_axis(2), joystick.get_axis(3), joystick.get_hat(0)[0], joystick.get_hat(0)[1]]

        for button in range(0,12):
                positions.append(joystick.get_button(button))

        #s.send(str(positions))
        print (positions)
        encode = str(positions)
        sendpos = str.encode(encode)
        s.send(sendpos)

        sleep(0.1) #Debounce controls





#s.close()
