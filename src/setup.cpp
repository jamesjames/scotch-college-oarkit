#include "ofApp.h"

//--------------------------------------------------------------
void ofApp::gamepadsetup(){
	ofxGamepadHandler::get()->enableHotplug();
	//CHECK IF THERE IS A GAMEPAD CONNECTED
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
	ofxDatGui* systemgui = new ofxDatGui( ofxDatGuiAnchor::TOP_LEFT );
	systemgui->setTheme(new ofxDatGuiThemeSmoke());
	systemgui->addHeader("System Controls", false);

	ofxDatGuiFolder* network = systemgui->addFolder("Network", ofColor::red);
	ofxDatGuiLabel* interfaces = network->addLabel(result);
	ofxDatGuiTextInput* iptext = network->addTextInput("Robot IP", "192.168.100.1");
	ofxDatGuiButton* connectButton = network->addButton("Connect");

	ofxDatGuiFolder* control = systemgui->addFolder("Control", ofColor::green);
	ofxDatGuiValuePlotter* leftxaxis = control->addValuePlotter("Left X axis", 0, 1);
	control->addValuePlotter("Left Y axis", 0, 1);
	control->addValuePlotter("Right X axis", 0, 1);
	control->addValuePlotter("Right Y axis", 0, 1);


}

/*
TO BE UPDATED
//--------------------------------------------------------------
void ofApp::camerasetup(){
	axisGrabber = ofPtr<ofxAxisGrabber>(new ofxAxisGrabber);
	axisGrabber->setCameraAddress(IP);
	grabber.setGrabber(axisGrabber);
	grabber.initGrabber(640,480);
}
*/
//--------------------------------------------------------------
void ofApp::tcpsetup(){
	//tcpClient.setup(ip, 5005);
}

//--------------------------------------------------------------
void ofApp::ipsetup(){
	myip.setup();
    vector<ofxMyIPAddress> list = myip.getList();
    stringstream s;
    for (vector<ofxMyIPAddress>::iterator o = list.begin(); o != list.end(); o++) {
        s << "interface: " << o->name << ", ip: " << o->address << endl;
    }
    result = s.str();
	std::cout << result << std::endl;
}
