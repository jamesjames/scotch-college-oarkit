// TODO
// * Write Camera System
// * Redo Gui
// * Comment Code
// * SSH?

#include "ofMain.h"
#include "ofApp.h"


extern string IP;
//========================================================================
int main(){
	//int sshconnect = "ssh pi@" + IP;
	//FILE *ssh = popen("ssh pi@", "rw");
	ofSetupOpenGL(1920,1080,OF_WINDOW);// <-------- setup the GL context

	// this kicks off the running of my app
	// can be OF_WINDOW or OF_FULLSCREEN
	// pass in width and height too:
	ofRunApp( new ofApp());

}
