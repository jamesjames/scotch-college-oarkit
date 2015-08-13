

import socket
import time
import ServoInterface as bot

TCP_IP = '192.168.100.1' # Set to Raspi IP
TCP_PORT = 5005
BUFFER_SIZE = 1024

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((TCP_IP, TCP_PORT))
s.listen(1)

def processCommand(input):
    
    turn = input[1:-1].split(",")
    try:
        turn[0] = float(turn[0])
        turn[1] = float(turn[1])
        return turn
    
    except:
        print "Unknown command"
        

while True:
    conn, addr = s.accept()
    print 'Connection address: ', addr

    while True:
        data = conn.recv(BUFFER_SIZE)
        if data: 
            command = processCommand(data)
            bot.spinAtPcSpeed(1, command[0])
            bot.spinAtPcSpeed(2, command[1])
            bot.spinAtPcSpeed(3, command[1])
            bot.spinAtPcSpeed(4, command[0])
        else:
            print "No data available"
            
        #print "received data:", data
        #conn.send(data)  # echo


#Stop servos when loop broken
command = [0,0]
bot.spinAtPcSpeed(1, command[0])
bot.spinAtPcSpeed(2, command[1])
bot.spinAtPcSpeed(3, command[1])
bot.spinAtPcSpeed(4, command[0])
conn.close()
