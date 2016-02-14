#include "ofApp.h"


//--------------------------------------------------------------
void ofApp::setup(){
	ofBackground(255,255,255);
	ofSetVerticalSync(true);
	ofApp::ipsetup();
	//ofApp::uisetup();
	ofApp::gamepadsetup();
	//ofApp::camerasetup();

	int joystickID = 0;

	int x = 340;
    int y = 100;


	ofxDatGuiComponent* component;

	plotter = new ofxDatGuiValuePlotter("value\nplotter", -100, 100);
    plotter->setSpeed(2.0f);
    plotter->setDrawMode(ofxDatGuiGraph::LINES);
    component = plotter;
    component->setPosition(x, y);
    components.push_back(component);


	ofxGLFWJoystick::one().printJoystickList();
}
//--------------------------------------------------------------
void ofApp::exit(){
	//connectButton.removeListener(this, &ofApp::connectButtonPressed);
	//playButton.removeListener(this, &ofApp::connectButtonPressed);
}

//--------------------------------------------------------------
void ofApp::DatGuiButtonEvent(ofxDatGuiButtonEvent e){

}

void ofApp::DatGuiTextInputEvent(ofxDatGuiTextInputEvent e) {

}

//--------------------------------------------------------------
void ofApp::update(){
	if(tcpClient.isConnected())
	{
		//connectButton.setName("Disconnect");
	}
	//grabber.update(); <-- part of camera update

	ofxGLFWJoystick::one().update();
	int joystickID = 0;

	float v = ofRandom(plotter->getMin(), plotter->getMax());
    plotter->setValue(v);
    for(int i=0; i<components.size(); i++) components[i]->update();


}

//--------------------------------------------------------------
void ofApp::draw(){
	ofSetHexColor(0xFFFFFF);
	for(int i=0; i<components.size(); i++) components[i]->draw();
	ofxGamepadHandler::get()->draw(200,200); // <-- redo as part of gui overhaul

	//grabber.draw(0,0); <-- part of camera update

	ofxGLFWJoystick::one().drawDebug(100,100);

	//safe access o joystick - slower
	int joystickID = 0;
	float joyX = ofxGLFWJoystick::one().getAxisValue(0, joystickID);
	float joyY = ofxGLFWJoystick::one().getAxisValue(1, joystickID);
	//getAxisValue() always returns a value in the [-1, 1] range


	//lets map the joystick to our window size
	float mappedX = ofMap(joyX, -1, 1, 0, ofGetWidth());
	float mappedY = ofMap(joyY, -1, 1, 0, ofGetHeight());
}

//--------------------------------------------------------------
void ofApp::axisChanged(ofxGamepadAxisEvent& e){
	cout << "AXIS " << e.axis << " VALUE " << ofToString(e.value) << endl;
}

//--------------------------------------------------------------
void ofApp::buttonPressed(ofxGamepadButtonEvent& e){

}

//--------------------------------------------------------------
void ofApp::buttonReleased(ofxGamepadButtonEvent& e){
	cout << "BUTTON " << e.button << " RELEASED" << endl;
}
//--------------------------------------------------------------
void ofApp::keyPressed(int key, string IP){

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
