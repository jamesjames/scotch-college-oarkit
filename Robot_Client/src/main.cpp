#include "ofMain.h"
#include "ofApp.h"





//========================================================================
int main( ){
    //FILE *ssh = popen("ssh pi@192.168.100.1", "rw");
    ofSetupOpenGL(1024,768,OF_WINDOW);			// <-------- setup the GL context

	// this kicks off the running of my app
	// can be OF_WINDOW or OF_FULLSCREEN
	// pass in width and height too:
	ofRunApp(new ofApp());

}
