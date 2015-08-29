#include "ofApp.h"

//--------------------------------------------------------------
void ofApp::setup(){
    ofBackground(255,255,255);
    ofSetVerticalSync(true);
    //ofApp::textboxsetup();
    ofApp::uisetup();
    ofApp::gamepadsetup();
    ofApp::camerasetup();
}
//--------------------------------------------------------------
void ofApp::exit(){
    connectButton.removeListener(this, &ofApp::connectButtonPressed);
    playButton.removeListener(this, &ofApp::connectButtonPressed);
}

//--------------------------------------------------------------
void ofApp::connectButtonPressed(){
    ofApp::tcpsetup();
}

//--------------------------------------------------------------
void ofApp::disconnectButtonPressed(){
    tcpClient.close();
}

//--------------------------------------------------------------
void ofApp::playButtonPressed(){
    //bool ofGstUtils::setPipelineWithSink(string pipeline, string sinkname = "sink",bool isStream = true)

}

//--------------------------------------------------------------
void ofApp::update(){
    if(tcpClient.isConnected())
    {
       connectButton.setName("Disconnect");
    }
    grabber.update();
}

//--------------------------------------------------------------
void ofApp::draw(){
    ofSetHexColor(0xFFFFFF);
/*
    ofPushMatrix();
	drawText();
	ofPopMatrix();
**/
    ofxGamepadHandler::get()->draw(200,200);

    grabber.draw(0,0);

    if( bHide )
    {
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
    if (e.button == 8){
        if (tcpClient.isConnected()){
            tcpClient.close();
        }
        else{
            ofApp::tcpsetup();
        }
    }
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
