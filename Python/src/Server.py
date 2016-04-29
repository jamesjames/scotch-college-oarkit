"""
The MIT License (MIT)

Copyright (c) Sat May 23 2015 Aren Leishman, Andrew Schaff, James Oakey, James Barr

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORTOR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

"""
#Set to True of testing (not connected to Pi)
test = False

import socket
import time
#Don't use this - there's a newer version of this
if not test:
    import ServoInterface as bot

#Set up server details
TCP_IP = '192.168.100.1' #Pi's IP 10.82.72.22
TCP_PORT = 5005
BUFFER_SIZE = 1024  # Normally 1024, but we want fast response

if not test:
    #Start a server and listen for connections if not testing
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind((TCP_IP, TCP_PORT))
    s.listen(1)


#Speed multipliers to be applied to processed inputs
cameraSpeed = 10
speedMultiplier_rightStick = 100.0
speedMultiplier_leftStick = 10.0
speedMultiplier_hat = 5.0

cameraPos = [0, 0, 0] #Camera position

command = [0.0, 0.0, 0.0, 0.0, False, False, False, False, False, False, False, False, False, False, False, False, False, False] #Default command state


"""
Command breakdown
[Joystick Axis 0, Joystick Axis 1, Joystick Axis 2, Joystick Axis 3, Hat Axis 0, Hat Axis 1]
[Right Joystick Horiz, Right Joystick Vert, Left Joystick Horiz, Right Joystick Vert, Hat Horiz, Hat Vert, button 1, ..., button 12]
"""

#Map inputs in order to gain a better comparison and control scheme
def map(x, in_min, in_max, out_min, out_max):
	return (x - in_min) * (out_max - out_min) // (in_max - in_min) + out_min

#Button functions
def printButton(): #Filler function for buttonList
    print "A button was pressed - nothing is bound to this button"

#Reset arm to default position (Straight up, centered, facing forwards)
def resetCamera():
    print "Resetting Camera"
    bot.moveToDegAngle(5, 0, cameraSpeed)
    bot.moveToDegAngle(6, 0, cameraSpeed)
    bot.moveToDegAngle(7, 0, cameraSpeed)

#Kill servos
def stop():
    print "Stopping Servos"
    bot.spinAtPcSpeed(1, 0)
    bot.spinAtPcSpeed(2, 0)
    bot.spinAtPcSpeed(3, 0)
    bot.spinAtPcSpeed(4, 0)

#Scripts to run for each button (printButton means its not linked to anything)
buttonScripts = [printButton, #Square
                 printButton, #Cross/X
                 printButton, #Circle/O
                 printButton, #Triangle
                 printButton, #L1
                 printButton, #R1
                 printButton, #L2
                 printButton, #R2
                 printButton, #Select
                 printButton, #Start
                 resetCamera, #Right Joystick
                 resetCamera, #Left Joystick
                 stop,  #Home/PS
                 printButton,
                 printButton
                 ]

#Copied from ServoInterface - determines wheel speed based on steering input
def throttleSteeringToLeftRight(inThrottle, inSteering):
	left = min(100, max(-100, inThrottle - inSteering));
	right = min(100, max(-100, inThrottle + inSteering));
	return [left, right]


#Process command input from socket
def processCommand(unprocessed_command):
    unprocessed_command = str(unprocessed_command) #Convert to a string if command comes in as a byte

    #try is used to ensure that any faulty strings to cause problems
    try:
        #Format incoming list in order to be read
        #print "unprocessed_command: " + str(unprocessed_command) #A bit of feedback

        #Split incoming string into a list and discard any extra input
        command = unprocessed_command[1:unprocessed_command.index("]")].split(",")

        #Go through command and remove any string bits (spaces and apostrophes)
        for element in range(len(command)):
            command[element] = command[element].replace("\'", "")
            command[element] = command[element].replace(" ", "")

        #print "Pre-converted Command: " + str(command)

        #Iterate through command and converted to floats zeroing out any noise on the way
        for element in range(len(command) - 2):
            command[element] = float(command[element]) #Convert to a floating point number
            if abs(command[element]) < 0.01:
                command[element] = 0.0 #Remove noise

        #Iterate through the remaining
        for element in range(len(command) - 2, len(command)):
            command[element] = int(float(command[element]))

        #Iterate through the back 12 elements (button data) and set them to True or False for On or Off, respectively
        for element in range(len(command) - 12, len(command)):
            if float(command[element]) == 1.0:
                command[element] = True
            else:
                command[element] = False


        #print command
        #Apply multipliers for each axis commands
        command[0] *= speedMultiplier_rightStick #Steering
        command[1] *= speedMultiplier_rightStick #Steering
        command[2] *= speedMultiplier_leftStick  #Camera
        command[3] *= speedMultiplier_leftStick  #Camera
        command[4] *= 1.0 #speedMultiplier_hat
        command[5] *= 1.0 #speedMultiplier_hat

        """ A bit of a breakdown
        command[0] - Right Stick Axis 0
        command[1] - Right Stick Axis 1
        command[2] - Left Stick Axis 0
        command[3] - Left Stick Axis 1
        command[4] - Hat Horizontal Axis
        command[5] - Hat Vertical Axis
        """
        print "Processed Command: " + str(command) + "\n"
        return command

    except:
        #Return a default position should the processing fail
        return [0.0, 0.0, 0.0, 0.0, False, False, False, False, False, False, False, False, False, False, False, False, False, False]


#Main network loop
while True:
    #Connect to other computers and print their IP
    conn, addr = s.accept()
    print 'Connection address: ', addr

    #Main operating loop
    while True:
        data = conn.recv(BUFFER_SIZE) #Download incoming data
        #print "Data: " + str(data)

        #Get and process data if available
        if data:
            command = processCommand(data)

        else:
            print "No data available"

        buttonList = command[-12:] #Set buttonList to the button values in command

        #Iterate through buttonList, calling the appropriate function for any active button
        for button in range(len(buttonList)):
            if buttonList[button]:
                buttonScripts[button]() #Call the appropriate function from buttonScripts


        #Send throttle and steering data to be prepared to g to the wheels
        turn = throttleSteeringToLeftRight(command[0], command[1])

        #Set servo speeds
        bot.spinAtPcSpeed(1, turn[0])
        bot.spinAtPcSpeed(2, turn[1])
        bot.spinAtPcSpeed(3, turn[1])
        bot.spinAtPcSpeed(4, turn[0])


        #Update camera positions
        cameraPos[0] += command[5] #Update target location for Servo 5
        cameraPos[1] += command[3] #Update target location for Servo 6
        cameraPos[2] += -command[2] #Update target location for Servo 7

        #Move camera to positions
        bot.moveToDegAngle(5, cameraPos[0], cameraSpeed)
        bot.moveToDegAngle(6, cameraPos[1], cameraSpeed)
        bot.moveToDegAngle(7, cameraPos[2], cameraSpeed)

        data = None #Clear data, just in case


#Stop servos when loop broken
bot.spinAtPcSpeed(1, 0)
bot.spinAtPcSpeed(2, 0)
bot.spinAtPcSpeed(3, 0)
bot.spinAtPcSpeed(4, 0)

conn.close() #Close connection to try and avoid 'address already in use' error
