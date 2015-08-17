# ScotchOARKit

###Client – Server Comunication

###Introduction:

This section outlines the client and server code for connecting the controller to the raspberry pi and driving the servos. The code is written in python and works by passing the raw controller values from the client via the TCP network to the server and the server then processes these inputs and sends them to the servos. The servos are driven by a separate python library that allows for easy interfacing with them.

###How To:

####Prerequisites
In order to start this task, you must have:
* A functioning wireless access point on the Raspberry Pi
* The following installed on the host computer
* Python 3.0
* Pygame
* Python-socket

####Materials needed
* USB Game Controller
* Host computer, Screen, Keyboard, and Mouse
* Robot (or at least a Raspberry Pi)

####Instructions
1.	Follow the links by the green flag to download client.py, server.py, and servointerface.py.
2.	Place the server.py and servointerface.py in a folder on the raspberry pi, the server and servointerface must be in the same folder.
3.	The client.py can placed anywhere on the host computer.
4.	Run the code on the raspberry pi by running `python <pathtoserver>` in a terminal on the pi. You can tell the server is running if the terminal says `Server Ready`
5.	Then run the code on the client by running `python <pathtoclient>` in a terminal on the host computer. You can tell the client is running if the values from the controller are being printed to the terminal.
6.	The programs should establish a networking link.
7.	You can then operate the robot by moving the sticks on the controller.

If you intend to use a slightly different setup than the standard you may need to change the IP addresses in the code before it needs.

1.	Open the server.py in the text editor of your choice and navigate to the line that says `TCP_IP = '192.168.100.1'`.
2.	Change the ip address to whatever your raspberry pi’s network IP is, this can be found by running `ifconfig` in the terminal.
3.	Now open the client.py in the text editor of your choice and navigate to the line that says `TCP_IP = '192.168.100.1'`.
4.	Change the line to match the IP located earlier.
5.	Now you can follow the instructions above.



####Resources:

https://github.com/QuickRecon/ScotchOARKit/blob/master/Server.py
https://github.com/QuickRecon/ScotchOARKit/blob/master/Client.py
https://github.com/QuickRecon/ScotchOARKit/blob/master/ServoInterface.py
