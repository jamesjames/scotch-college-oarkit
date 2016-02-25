inputs = [] #Somewhere to store the current status (not really necessary)

pygame = None

buttonIDs = {0 : "Square",
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

#Start up pygame if necessary
def startPygame():
    global pygame
    import pygame

    #Initialize all libraries and the joystick
    try:
        pygame.init()
        pygame.joystick.init()
        
    except:
        print "Error: PyGame failed to intialize!"

#Stop pygame if necessary
def stopPygame():
    pygame.quit()
    
#Start the joystick
def startJoystick():
    print "Initializing gamepad..."
    global joystick
    
    try:
        joystick = pygame.joystick.Joystick(0)

    except:
        print "Error: Joystick not found!"

    try:
        joystick.init()
        print "Gamepad ready!"
        
    except:
        print "Error: Joystick failed to initialize!"

    
#Duplicate of the Arduino map function
def map(x, in_min, in_max, out_min, out_max):
        return (x - in_min) * (out_max - out_min) // (in_max - in_min) + out_min
    
#Convert joystick value to a hex value (-1 to 1 mapped to 0 to F)
def toHex(num):
        mapped = int(map(float(num), -1.0, 1.0, 0, 15)) #Map value between 0 and 15
        return ["0", "1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"][mapped] #Convert to hex

#Get the state of all buttons/hats/sticks on the gamepad
def getState():
    global inputs
    pygame.event.get()
    
    inputs = [toHex(joystick.get_axis(0)), toHex(joystick.get_axis(1)), toHex(joystick.get_axis(2)), toHex(joystick.get_axis(3)), toHex(joystick.get_hat(0)[0]), toHex(joystick.get_hat(0)[1])]

    for button in range(0,12):
                inputs.append(joystick.get_button(button))

    dataMsg = ""
    
    for datum in inputs:
        dataMsg += str(datum)
        
    return (inputs, dataMsg)

#Automatically start the necessary functions (so the user doesn't have to)
startPygame()
startJoystick()
