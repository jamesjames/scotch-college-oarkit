#include "ofApp.h"

string IP;

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
        cout << "Gamepad Failed to detect.(is it pluged in?)" << endl;
    }
}

//--------------------------------------------------------------
void ofApp::uisetup(){
    connectButton.addListener(this, &ofApp::connectButtonPressed);
    playButton.addListener(this, &ofApp::playButtonPressed);
    disconnectButton.addListener(this, &ofApp::disconnectButtonPressed);

    networkcontrol.setup();
    networkcontrol.add(connectButton.setup("Connect"));
    networkcontrol.add(disconnectButton.setup("Disconnect"));
    networkcontrol.add(playButton.setup("Start Stream"));

    bHide = true;
}

//--------------------------------------------------------------
void ofApp::camerasetup(){
    axisGrabber = ofPtr<ofxAxisGrabber>(new ofxAxisGrabber);
	axisGrabber->setCameraAddress(IP);
	grabber.setGrabber(axisGrabber);
	grabber.initGrabber(640,480);
}

void ofApp::tcpsetup(){
    tcpClient.setup(IP, 5005);
}

void ofApp::setIP(string newIP)
{
    IP = newIP;
}
