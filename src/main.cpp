#include "ofMain.h"
#include "ofApp.h"


extern string IP;
//========================================================================
int main(){
    ofSetupOpenGL(1920,1080,OF_WINDOW);
	ofApp *myapp = new ofApp();
	myapp->setIP(IP);
	ofRunApp(myapp);

}
