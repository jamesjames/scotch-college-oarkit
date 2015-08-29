#include "ofMain.h"
#include "ofApp.h"


extern string IP;
//========================================================================
int main(){
    cout << "What is the IP of the Robot?" << endl;
    cin >> IP;
    //int sshconnect = "ssh pi@" + IP;
    //FILE *ssh = popen("ssh pi@", "rw");
    ofSetupOpenGL(1024,768,OF_WINDOW);			// <-------- setup the GL context

	// this kicks off the running of my app
	// can be OF_WINDOW or OF_FULLSCREEN
	// pass in width and height too:
	ofApp *myapp = new ofApp();
	myapp->setIP(IP);
	ofRunApp(myapp);

}
