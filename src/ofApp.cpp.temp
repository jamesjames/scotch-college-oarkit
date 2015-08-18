#include "ofApp.h"

//--------------------------------------------------------------
void ofApp::setup(){
    ofBackground(255,255,255);
    ofSetVerticalSync(true);

    connectButton.addListener(this, &ofApp::connectButtonPressed);
    playButton.addListener(this, &ofApp::playButtonPressed);

    networkcontrol.setup();
    networkcontrol.add(connectButton.setup("Connect"));

    videocontrol.setup();
    videocontrol.add(playButton.setup("Start Stream"));

    bHide = true;

    ofxGamepadHandler::get()->enableHotplug();
//CHECK IF THERE EVEN IS A GAMEPAD CONNECTED
    if(ofxGamepadHandler::get()->getNumPads()>0)
    {
        ofxGamepad* pad = ofxGamepadHandler::get()->getGamepad(0);
        ofAddListener(pad->onAxisChanged, this, &ofApp::axisChanged);
        ofAddListener(pad->onButtonPressed, this, &ofApp::buttonPressed);
        ofAddListener(pad->onButtonReleased, this, &ofApp::buttonReleased);
        cout << "Gamepad Loaded Sucessfully" << endl;
    }
    else{
        cout << "Gamepad Failed to load (is it pluged in?)" << endl;
    }
    cameraStream.loadMovie("/dev/stdin");

    if (cameraStream.isLoaded() == false){
        cout << "Camera Stream Failed to load" << endl;
    }
}
//--------------------------------------------------------------
void ofApp::exit(){
    connectButton.removeListener(this, &ofApp::connectButtonPressed);
    playButton.removeListener(this, &ofApp::connectButtonPressed);
}

//--------------------------------------------------------------
void ofApp::connectButtonPressed(){

    cameraStream.loadMovie("/dev/stdin");

    /*
    if (tcpClient.isConnected())
    {
        tcpClient.setup("192.168.100.1", 5005);
    }
    else
    {
        tcpClient.close();
    }
    **/

}

//--------------------------------------------------------------
void ofApp::playButtonPressed(){

}
//--------------------------------------------------------------
void ofApp::update(){
/*
    if (cameraStream.isLoaded()){
        cameraStream.update();
    }
    else
    {
        cameraStream.loadMovie("/dev/stdin");
    }
    **/
    if(tcpClient.isConnected())
    {
       connectButton.setName("Disconnect");
    }
}

//--------------------------------------------------------------
void ofApp::draw(){
    ofSetHexColor(0xFFFFFF);
    cameraStream.draw(400,20);

    ofxGamepadHandler::get()->draw(200,200);

    if( bHide )
    {
		videocontrol.draw();
		networkcontrol.draw();
	}
}

//--------------------------------------------------------------
void ofApp::axisChanged(ofxGamepadAxisEvent& e){
    cout << "AXIS " << e.axis << " VALUE " << ofToString(e.value) << endl;
}

//--------------------------------------------------------------
void ofApp::buttonPressed(ofxGamepadButtonEvent& e){
    cout << "BUTTON " << e.button << " PRESSED" << endl;
}

//--------------------------------------------------------------
void ofApp::buttonReleased(ofxGamepadButtonEvent& e){
    cout << "BUTTON " << e.button << " RELEASED" << endl;
}
//--------------------------------------------------------------
void ofApp::keyPressed(int key){

}

//--------------------------------------------------------------
void ofApp::keyReleased(int key){

}

//--------------------------------------------------------------
void ofApp::mouseMoved(int x, int y ){

}

//--------------------------------------------------------------
void ofApp::mouseDragged(int x, int y, int button){

}

//--------------------------------------------------------------
void ofApp::mousePressed(int x, int y, int button){

}

//--------------------------------------------------------------
void ofApp::mouseReleased(int x, int y, int button){

}

//--------------------------------------------------------------
void ofApp::windowResized(int w, int h){

}

//--------------------------------------------------------------
void ofApp::gotMessage(ofMessage msg){

}

//--------------------------------------------------------------
void ofApp::dragEvent(ofDragInfo dragInfo){

}
