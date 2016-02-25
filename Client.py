#Connection details
TCP_IP = "localhost"
TCP_PORT = 5006

connect = True #Set to true to connect to Pi

#Ensure that we can import all of the libraries in BotLib
import sys
sys.path.insert(0, 'BotLib/')

import GamepadInterface as GP #Import the gamepad interface

import socket

#Connect to server if 'connect' is True
if connect:
    conn = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    conn.connect((TCP_IP, TCP_PORT))

#Send the gamepad data to the Pi
def usegamepad():
    while True:
        try:
            gamepadData = GP.getState()[1]
            print gamepadData
            if connect:
                conn.send("G" + gamepadData)
                
                while not conn.recv(1024): pass #Wait until the acknoledgement comes
            
        except KeyboardInterrupt:
            print "\nGamepad input halted"
            break

#Send manual commands
def sendManualCmd():
    while True:
        try:
            if connect:
                conn.send(raw_input("Input command: "))
                
        except KeyboardInterrupt:
            return
        
#Close the connection
def close():
    global conn
    conn.close()


#usegamepad()
sendManualCmd()

if raw_input("\nDo you want to stop (y/n)?: ").lower() == "y":
    GP.stopPygame()
    
    if connect:
        conn.send("BREAK")

    close()
