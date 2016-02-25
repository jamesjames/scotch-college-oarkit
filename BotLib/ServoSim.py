import pygame

""" Not used
#These variables store speeds
Servo1 = 0  #Front Right
Servo2 = 0  #Front Left
Servo3 = 0  #Back Left
Servo4 = 0  #Back Right

#These variables store positions
Servo5 = 0  #Base
Servo6 = 0  #Elbow
Servo7 = 0  #Pan
"""

#Just some fake functions for simulation purposes
def convertServoID(servoID):
    pass

def spinAtPcSpeed(servoID, speed):
    pass

def moveToDegAngle(servoID, position):
    pass

def printStatus():
    print "Wheels"
    print "Servo 1:", Servo1
    print "Servo 2:", Servo2
    print "Servo 3:", Servo3
    print "Servo 4:", Servo4

    print "\nServos"
    print "Servo 5:", Servo5
    print "Servo 6:", Servo6
    print "Servo 7:", Servo7


def throttleSteeringToLeftRight(inThrottle, inSteering):
	left = min(100, max(-100, inThrottle - inSteering)); 
	right = min(100, max(-100, inThrottle + inSteering)); 
	return (left, right)

class Colors():
    black = (0,0,0)
    white = (255,255,255)
    gray = (127, 127, 127)
    
    red = (255, 0, 0)
    green = (0,255,0)
    blue = (0,0,255)

    orange = (225, 115, 0)
    yellow = (225, 225, 0)
    navy = (0, 0, 180)

    VenusYellow = (200,200,200)
    EarthBlue = (80, 120, 200)
    MarsRed = (200, 45, 0)

"""
class EmuVisualizer():
    size = width, height = 750, 750
    screen = pygame.display.set_mode(size)
    pygame.display.set_caption("OARKit EMU Visualization")
    
    def __init__(self):
        pass

    def update(self, robot, arm):

        pygame.event.get()
        pygame.display.flip()
        pygame.fill(Colors.white)
"""
