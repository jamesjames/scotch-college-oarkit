#include "ofApp.h"
#include "setup.h"

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

void ofApp::uisetup(){
    connectButton.addListener(this, &ofApp::connectButtonPressed);
    playButton.addListener(this, &ofApp::playButtonPressed);

    networkcontrol.setup();
    networkcontrol.add(connectButton.setup("Connect"));
    networkcontrol.add(playButton.setup("Start Stream"));

    bHide = true;
}

void ofApp::textboxsetup(){
    text="192.168.100.1";
	position=0;
	cursorx=0;
	cursory=0;
}
