#include "ofApp.h"

string IP;
bool gamepad = true;
bool camerainitalized = true;

//--------------------------------------------------------------
void ofApp::gamepadsetup(){
    ofxGamepadHandler::get()->enableHotplug();
    //CHECK IF THERE EVEN IS A GAMEPAD CONNECTED
    if(ofxGamepadHandler::get()->getNumPads()>0)
    {
        ofxGamepad* pad = ofxGamepadHandler::get()->getGamepad(0);
        ofAddListener(pad->onAxisChanged, this, &ofApp::axisChanged);
        ofAddListener(pad->onButtonPressed, this, &ofApp::buttonPressed);
        ofAddListener(pad->onButtonReleased, this, &ofApp::buttonReleased);
        cout << "Gamepad Detected" << endl;
    }
    else{
        ofSystemAlertDialog("Warning: Gamepad not detected, sending movement commands to the robot has been disabled.");
        gamepad = false;
    }
}

//--------------------------------------------------------------
void ofApp::uisetup(){
    playButton.addListener(this, &ofApp::playButtonPressed);
    stopButton.addListener(this, &ofApp::stopButtonPressed);
    ipButton.addListener(this, &ofApp::ipButtonPressed);

    if (gamepad = false){
        connectButton.addListener(this, &ofApp::connectButtonPressed);
        disconnectButton.addListener(this, &ofApp::disconnectButtonPressed);
    }

    networkcontrol.setup();

    networkcontrol.add(playButton.setup("Start Stream"));
    networkcontrol.add(stopButton.setup("Stop Stream"));
    networkcontrol.add(ipButton.setup("Change IP"));
    if (gamepad = false){
        networkcontrol.add(connectButton.setup("Connect"));
        networkcontrol.add(disconnectButton.setup("Disconnect"));
    }
    bHide = true;
}

//--------------------------------------------------------------
void ofApp::camerasetup(){
    if (camerainitalized){
        axisGrabber = ofPtr<ofxAxisGrabber>(new ofxAxisGrabber);
        axisGrabber->setCameraAddress(IP);
        camerainitalized = false;
    }
	grabber.setGrabber(axisGrabber);
	grabber.initGrabber(640,480);
}


//--------------------------------------------------------------
void ofApp::tcpsetup(){
    if (gamepad){
        FILE *ssh = popen("ssh pi@192.168.1.5", "w");
        fprintf(ssh, "python server.py", 10);
        sleep(10);
        tcpClient.setup(IP, 5005);
    }
}

//--------------------------------------------------------------
void ofApp::setIP(string newIP){
    IP = newIP;
}

//--------------------------------------------------------------
void ofApp::resetIP(){
    IP = ofSystemTextBoxDialog("IP", IP);
    ofApp::setIP(IP);
    //reset all connections.
    ofApp::tcpClient.close();
    ofApp::tcpClient.setup(IP, 5005);
}
