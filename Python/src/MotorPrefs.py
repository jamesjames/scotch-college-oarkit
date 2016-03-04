"""
Servo Numbers and Positions
 Front Left - 1
 Front Right - 2
 Back Left - 3
 Back Right - 4

 Arm Base - 5
 Arm Top - 6
 Arm Pan - 7
"""

isSimulation = True #Setup to run a simulator

if isSimulation:
    import ServoSim as sp
else:
    import ServoInterface as sp

#Object to control motors
class MotorObject():
    def __init__(self, servoID):
        self.servoID = servoID
        self.speed = 0
        
    def setSpeed(self, speed):
        self.speed = speed
        sp.spinAtPcSpeed(self.servoID, speed)

    def getSpeed(self):
        return self.speed

#Object to control servos
class ServoObject():
    """
    To Do
         - Set servo 0
    """
    
    def __init__(self, servoID):
        self.servoID = servoID
        self.position = 0
        
    def __str__(self):
        return self.position
    
    def setPosition(self, position):
        self.position = position
        sp.moveToDegAngle(self.servoID, position)

#Object to handle multiple servos as one
class TrackObject():
    def __init__(self, attachedServos):
        """ attachedServos is a list of ServoObjects """
        self.servos = attachedServos
        self.speed = 0

    def setSpeed(self, speed):
        self.speed = speed
        for servo in self.servos:
            servo.setSpeed(speed)

    def getSpeed(self):
        return self.speed

#Control the robot's forward and backward motion (designed for a four-wheeled vehicle)
class Truck():
    def __init__(self, FrontRight, FrontLeft, BackRight, BackLeft):
        """ Create the object that controls the robot """
        self.FR = FrontRight
        self.FL = FrontLeft
        self.BR = BackRight
        self.BL = BackLeft

        self.steering = (0,0)

    def __str__(self):
        return "FR: " + str(self.FR.getSpeed()) + "\nFL: " + str(self.FL.getSpeed()) + "\nBR: " + str(self.BR.getSpeed()) + "\nBL: " + str(self.BL.getSpeed()) 
    
    def AllStop(self):
        """ Stop all wheels """
        self.FR.setSpeed(0)
        self.FL.setSpeed(0)
        self.BR.setSpeed(0)
        self.BL.setSpeed(0)
        print "Truck - Emergency Stop Command Executed"
        
    def sendInput(self, throttle, steering):
        """ Throttle between -100 <-> 100 (Rev <-> Fwd) - Steering between -100 <-> 100 (L <-> R) """
        self.steering = sp.throttleSteeringToLeftRight(throttle, steering)

        self.FR.setSpeed(self.steering[1])
        self.FL.setSpeed(-self.steering[0])
        self.BR.setSpeed(self.steering[1])
        self.BL.setSpeed(-self.steering[0])
        
#Similar to truck, but can have multiple servos acting as a tank track
class Tank():
    def __init__(self, LeftTrack, RightTrack):
        """ Create an object that drives like a tank """
        
        self.LeftTrack = LeftTrack
        self.RightRight = RightTrack
        self.steering = (0,0)

    def __str__(self):
        return "Left Speed: " + str(self.LeftTrack.getSpeed()) + "\nRight Speed: " + str(self.RightTrack.getSpeed())

    def AllStop(self):
        self.LeftTrack.setSpeed(0)
        self.RightTrack.setSpeed(0)
        print "Tank - Emergency Stop Command Executed"

    def sendInput(self, throttle, steering):
        """ Throttle between -100 <-> 100 (Rev <-> Fwd) - Steering between -100 <-> 100 (L <-> R) """
        self.steering = sp.throttleSteeringToLeftRight(throttle, steering)
        
        self.RightTrack.setSpeed(self.steering[1])
        self.LeftTrack.setSpeed(-self.steering[0])

#Control the arm (set positions)
class Arm():
    #Further work pending further development of arm hardware design
    def __init__(self, Base, Elbow, Pan):
        """ Create the arm object.  Base, Elbow, Pan are ServoObjects """
        self.base = Base
        self.elbow = Elbow
        self.pan = Pan

    def setHeight(self, height):
        #Inverse Kinematics?
        pass

    #Set a azimuth position
    def setAzimuth(self, pan): pass

    #Change height
    def deltaHeight(self, change): pass

    #Change azimuth
    def deltaAzimuth(self, change): pass

    #Set shoulder position
    def pivotShoulder(self, newPos): pass

    #Set elbow position
    def pivotElbow(self, newPos): pass

#A class to deal with button info
class ActionController():
    #What index corresponds to what button
    lookupList = ["Square", "X", "Triangle", "L1", "R1", "L2", "R2", "Select", "Start", "LeftClick", "RightClick", "Home/PS"]
    
    def __init__(self, bindingList):
        self.bindingList = bindingList #What function is bound to what button

    #Take in a list of commands and act on each one
    def actOnCmd(self, cmdList):
        for idx in range(len(cmdList)):
            boundFunction = self.bindingList[idx]   #Retrive the function this button is bound to
            buttonData = cmdList[idx]               #Get the state of the button (True/False)

            try:
                if buttonData: boundFunction()      #Call the function if it's button has been pressed
                
            except TypeError:
                print "Button '" + self.lookupList[idx] + "' has not been setup yet!"
    
#Set up the wheels
front_right_wheel = MotorObject(1)
front_left_wheel = MotorObject(2)
back_right_wheel = MotorObject(3)
back_left_wheel = MotorObject(4)

#Set up the arm servos
BaseServo = ServoObject(5)
ElbowServo = ServoObject(6)
PanServo = ServoObject(7)
