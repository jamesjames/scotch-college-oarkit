"""
To Do
     - Manual control of each servo
     - Reimport functionality
     - Change to base-20?
     - Move to Java
"""

#Some flags for testing
OnRobot = False  #Testing Software = False, Using hardware = True
RunServer = True    #Run the server = True, Use a local command interface = False

import socket
#import yaml
import sys
import time
from java.util import Properties
from java.lang import Thread
from java.net import InetAddress;
sys.path.insert(0, 'python') #Allows us to import stuff from BotLib

x = Properties()
x.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("com/scotch/OARKit/assets/properties/default.properties"))
#print(x.getProperty("insideDev"))

##MotorPrefs = imp.load_source('MotorPrefs', './python/MotorPrefs.py')
import MotorPrefs

MotorPrefs.isSimulation = OnRobot #Simulation flag

#Build the robot's truck
robot = MotorPrefs.Truck(MotorPrefs.front_right_wheel,
                         MotorPrefs.front_left_wheel,
                         MotorPrefs.back_right_wheel,
                         MotorPrefs.back_left_wheel
                         )

#Build the robot arm
robotArm = MotorPrefs.Arm(MotorPrefs.BaseServo,
                          MotorPrefs.ElbowServo,
                          MotorPrefs.PanServo)

#Setup the buttonBindings
buttonBindings = [
    
    None,
    None,
    None,
    None,
    None,
    None,
    None,
    None,
    None,
    None,
    None,
    None

    ]

#Setup the action controller
buttonController = MotorPrefs.ActionController(buttonBindings)


#A class to handle the TCP connections (cleans things up)
class ConnectionObject():
    def __init__(self, TCP_IP = None, TCP_PORT = 5006):
        #Automatically assign the IP if the field is left empty
        if x.getProperty("insideDev") == "false":
            # MAKE SURE THAT THE INET INTERFACE IS THE MAIN ONE
            TCP_IP = InetAddress.getLocalHost().getHostAddress()
            print("Running outside of Dev - IP = "+TCP_IP)
        else:
            TCP_IP = 'localhost'
            print("Running in Dev")
            
        self.TCP_IP = TCP_IP
        self.TCP_PORT = TCP_PORT
        self.BUFFER_SIZE = 1024  # Normally 1024, but we want fast response

    def startServer(self):
        #Start a server and listen for connections if not testing
        self.s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.s.bind((self.TCP_IP, self.TCP_PORT))
        self.s.listen(1)

    def closeConnection(self):
        self.conn.close()

    def tryConnection(self):
        self.conn, self.addr = self.s.accept()
        print "Connected to " + str(self.addr[0])

    def receiveCommand(self):
        return self.conn.recv(self.BUFFER_SIZE)

    def sendCommand(self, data):
        self.conn.send(str(data))

#Start the server if RunServer is set to True
if RunServer:
    connection = ConnectionObject()
    connection.startServer()
    
#Duplicate of the Arduino map function
def map(x, in_min, in_max, out_min, out_max):
        return (x - in_min) * (out_max - out_min) // (in_max - in_min) + out_min

#Execute manual commands
def ExecuteManualCMD(data):
    print "This doesn't do anything yet"

#Execute system commands
def ExecuteSystemCMD(data):
    print "This doesn't do anything yet"
    
#Take the command string from the connection and act on it
def interpretGamepadData(data):
    axisData_in = list(data[:6])
    buttonData_in = list(data[6:])

    #Convert the data to the forms required by the various control functions
    axisData = [map(int(x, base=16), 0, 15, -100, 100) for x in axisData_in]    #Hexadecimal to decimal percentage
    buttonData = [[False, True][int(x)] for x in buttonData_in]                 #0,1 to True,False

    #Zero out axis data if it came in as +/- 7
    axisData = [0 if abs(x) <= 10 else x for x in axisData] #Using a ternary operator to figure out if the absolute value of the axis data is less than 10

    #Apply all gamepad data
    """
    <<<<<<<< Assign axes here >>>>>>>>
    axisData[0] - Left X - Steering
    axisData[1] - Left Y - Throttle
    axisData[2] - Right X - Camera Pan
    axisData[3] - Right Y - Camera elevation
    axisData[4] - Hat X - None
    axisData[5] - Hat Y - None
    """

    robot.sendInput(axisData[1], axisData[0])   #Send the steering and throttle data to the robot
    robotArm.deltaAzimuth(axisData[2])          #Change the camera's azimuth
    robotArm.deltaHeight(axisData[3])           #Change the arm's height

    #Apply all button data
    buttonController.actOnCmd(buttonData)
    print robot
    #print "Axis Data: " + str(axisData)
    #print "Button Data: " + str(buttonData)

s = "7F7777" + "0" * 11 + "1"
i = interpretGamepadData

#Convert the incoming command signal to a list and execute commands
def decodeIncomingData(data):
    global JoystickPos

    print "Command Received: " + str(data)
    
    try:
        if data[0] == "G":
            #goodData = re.match("[GXM]\d{18}", data).group(0)
            
            #print data[1:] + " is a gamepad command"
            interpretGamepadData(data[1:]) #Send the gamepad data to a processor

        elif data[0] == "M":
            #print data[1:] + " is a manual command"
            ExecuteManualCMD(data[1:])          #Execute manual commands

        elif data[0] == "X":
            #print data[1:] + " is a system command"
            ExecuteSystemCMD(data[1:])          #Execute system commands
            
        elif data[0] == "E":
            print data[1:]                      #Echo the data to the terminal
        
        else:
            print data[0] + " is not recognized as a valid flag" #Print an error of the command isn't recognized

    except:
        print("Command execution failed")

if "debug" in str(sys.argv):
    print("Starting server in Debug Mode")
    RunServer = False

run = True
while run:
    if RunServer:
        print "Disconnected - Waiting for connection"
        connection.tryConnection()

        while True:
            data = connection.receiveCommand()
            
            if not data:
                print "Connection lost"
                break #Client has probably disconnected
            
            if "STOPSERVER" in data:
                print "Server shutting down"
                time.sleep(5)
                run = False
                break

            decodeIncomingData(data)
    else:
        while True:
            data = raw_input("<Man. CMD> ")

            decodeIncomingData(data)

