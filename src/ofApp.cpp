#include "ofApp.h"
#include "setup.h"

//--------------------------------------------------------------
void ofApp::setup(){
    ofBackground(255,255,255);
    ofSetVerticalSync(true);
    ofApp::textboxsetup();
    ofApp::uisetup();
    ofApp::gamepadsetup();
}
//--------------------------------------------------------------
void ofApp::exit(){
    connectButton.removeListener(this, &ofApp::connectButtonPressed);
    playButton.removeListener(this, &ofApp::connectButtonPressed);
}

//--------------------------------------------------------------
void ofApp::connectButtonPressed(){
    if (tcpClient.isConnected())
    {
        tcpClient.setup("192.168.100.1", 5005);
    }
    else
    {
        tcpClient.close();
    }
}

//--------------------------------------------------------------
void ofApp::playButtonPressed(){
    //bool ofGstUtils::setPipelineWithSink(string pipeline, string sinkname = "sink",bool isStream = true)

}
//--------------------------------------------------------------
void ofApp::update(){
/*
    if (cameraStream.isLoaded()){
        cameraStream.update();
    }
    else
    {
        cameraStream.stream("/dev/stdin");
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

    ofPushMatrix();
	drawText();
	ofPopMatrix();

    ofxGamepadHandler::get()->draw(200,200);

    if( bHide )
    {
		networkcontrol.draw();
	}
}

void ofApp::drawText() {
	ofScale(5,5);
	ofDrawBitmapString(text, 10,10);

	ofPushStyle();
	float timeFrac = 255.0f * sin(3.0f * ofGetElapsedTimef());
	ofSetColor(timeFrac,timeFrac,timeFrac);
	ofSetLineWidth(3.0f);
	ofLine(cursorx*8 + 10, 13.7*cursory, cursorx*8 + 10, 10+13.7*cursory);
	ofPopStyle();
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
    typeKey(key);
}

void ofApp::typeKey(int key) {
	//add charachter
	if (key >=32 && key <=126) {
		text.insert(text.begin()+position, key);
		position++;
	}

	if (key==OF_KEY_RETURN) {
		text.insert(text.begin()+position, '\n');
		position++;
	}

	if (key==OF_KEY_BACKSPACE) {
		if (position>0) {
			text.erase(text.begin()+position-1);
			--position;
		}
	}

	if (key==OF_KEY_DEL) {
		if (text.size() > position) {
			text.erase(text.begin()+position);
		}
	}

	if (key==OF_KEY_LEFT)
		if (position>0)
			--position;

	if (key==OF_KEY_RIGHT)
		if (position<text.size()+1)
			++position;

	//for multiline:
	cursorx = cursory = 0;
	for (int i=0; i<position; ++i) {
		if (*(text.begin()+i) == '\n') {
			++cursory;
			cursorx = 0;
		} else {
			cursorx++;
		}
	}

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
