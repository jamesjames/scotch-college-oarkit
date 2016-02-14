#pragma once

#include <stdlib.h>
#include <libssh/libssh.h>
#include <errno.h>
#include <string.h>
#include <iostream>

#include "ofxGamepad.h"
#include "ofxGamepadHandler.h"
#include "ofxGui.h"
#include "ofxNetwork.h"
#include "ofMain.h"
#include "ofxOpenCv.h"
#include "ofxDatGui.h"
#include "ofxMyIP.h"
#include "ofxGLFWJoystick.h"

class ofApp : public ofBaseApp{
	public:
		//core OpenFrameworks functions
		void setup();
		void update();
		void draw();
		void exit();

		//setup functions
		void uisetup();
		void gamepadsetup();
		void textboxsetup();
		void camerasetup();
		void tcpsetup();
		void ipsetup();

		//Gamepad Events
		void axisChanged(ofxGamepadAxisEvent &e);
		void buttonPressed(ofxGamepadButtonEvent &e);
		void buttonReleased(ofxGamepadButtonEvent &e);

		//Input Events
		void keyPressed(int key, string IP);
		void keyReleased(int key);
		void mouseMoved(int x, int y );
		void mouseDragged(int x, int y, int button);
		void mousePressed(int x, int y, int button);
		void mouseReleased(int x, int y, int button);
		void windowResized(int w, int h);
		void dragEvent(ofDragInfo dragInfo);
		void gotMessage(ofMessage msg);

		//Gui Events
		void DatGuiButtonEvent(ofxDatGuiButtonEvent e);
		void DatGuiTextInputEvent(ofxDatGuiTextInputEvent e);

		//Gui Definitions
		//ofxDatGuiButton* iptext;
		vector<ofxDatGuiComponent*> components;
		ofxDatGuiValuePlotter* plotter;


		//Network Definitions
		ofxTCPClient tcpClient;

	private:
    	ofxMyIP myip;
    	string result;
};
