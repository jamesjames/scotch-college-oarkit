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
#include "ofGstUtils.h"

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
        void setIP(string newIP);
        void resetIP(string newIP);

        //Gamepad listeners
        void axisChanged(ofxGamepadAxisEvent &e);
        void buttonPressed(ofxGamepadButtonEvent &e);
        void buttonReleased(ofxGamepadButtonEvent &e);

        //input listeners
		void keyPressed(int key, string IP);
		void keyReleased(int key);
		void mouseMoved(int x, int y );
		void mouseDragged(int x, int y, int button);
		void mousePressed(int x, int y, int button);
		void mouseReleased(int x, int y, int button);
		void windowResized(int w, int h);
		void dragEvent(ofDragInfo dragInfo);
		void gotMessage(ofMessage msg);

        //ui listener
		void connectButtonPressed();
		void playButtonPressed();
		void disconnectButtonPressed();

        //ui variable
        bool bHide;

        //textbox functions
		void	drawText();
		void	typeKey(int key);
		string	text;
		int		position;
		int		cursorx, cursory;

        //ui definitions
		ofxButton connectButton;
		ofxButton disconnectButton;
        ofxButton playButton;
        ofxPanel networkcontrol;

        //network definitions
        ofxTCPClient tcpClient;
};
