#pragma once

#include <stdlib.h>
#include <libssh/libssh.h>
#include <errno.h>
#include <string.h>

#include "ofxGamepad.h"
#include "ofxGamepadHandler.h"
#include "ofxGui.h"
#include "ofxNetwork.h"
#include "ofMain.h"
#include "ofxOpenCv.h"

class ofApp : public ofBaseApp{

	public:
		void setup();
		void update();
		void draw();

        void exit();

        void axisChanged(ofxGamepadAxisEvent &e);
        void buttonPressed(ofxGamepadButtonEvent &e);
        void buttonReleased(ofxGamepadButtonEvent &e);

		void keyPressed(int key);
		void keyReleased(int key);
		void mouseMoved(int x, int y );
		void mouseDragged(int x, int y, int button);
		void mousePressed(int x, int y, int button);
		void mouseReleased(int x, int y, int button);
		void windowResized(int w, int h);
		void dragEvent(ofDragInfo dragInfo);
		void gotMessage(ofMessage msg);

		void connectButtonPressed();
		void playButtonPressed();

		bool bHide;

		ofxButton connectButton;
        ofxButton playButton;

		ofxPanel networkcontrol;
		ofxPanel videocontrol;

		ofVideoPlayer   cameraStream;


        ofxTCPClient tcpClient;


};
