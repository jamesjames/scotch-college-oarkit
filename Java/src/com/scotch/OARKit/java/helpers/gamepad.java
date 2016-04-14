package com.scotch.OARKit.java.helpers;

import com.kenai.jaffl.struct.Struct;
import com.scotch.OARKit.java.*;
import com.scotch.OARKit.java.Command.Interpreter;
import com.scotch.OARKit.java.helpers.JInputJoystick;
import javafx.scene.control.Button;
import net.java.games.input.*;
import net.java.games.input.Controller;


public class gamepad {

    public static boolean connected = true;

    public float leftstickx;
    public float leftsticky;
    public float lefttrigger;

    public float rightstickx;
    public float rightsticky;
    public float righttrigger;

    public int HatSwitchPosition = 0;
    public float POV = 0;

    public boolean ButtonX;
    public boolean ButtonY;
    public boolean ButtonA;
    public boolean ButtonB;
    public boolean ButtonRT;
    public boolean ButtonRB;
    public boolean ButtonLT;
    public boolean ButtonLB;
    public boolean ButtonUp;
    public boolean ButtonRight;
    public boolean ButtonDown;
    public boolean ButtonLeft;
    public boolean ButtonRightStick;
    public boolean ButtonLeftStick;

    public static int numberOfButtons;

    public JInputJoystick gamepad;

    public void gamepad(){
        gamepad = new JInputJoystick(Controller.Type.GAMEPAD, Controller.Type.STICK);
        gamepad.pollController();
        numberOfButtons = gamepad.getNumberOfButtons();

        // Check if the controller was found.
        if(gamepad.isControllerConnected()/*&&gamepad.getControllerType().equals("Gamepad")/**/){
            Logger.info("Controller found, printing details:");
            Logger.info(gamepad.getControllerName());
            Logger.info(gamepad.getControllerType());
            Logger.info("This controller has " + numberOfButtons + " buttons");
        } else {
            connected = false;
            Logger.info("No controller found!");
        }
    }

    public void pollgamepad(){
        if(!gamepad.pollController()) {
            Logger.info("Controller disconnected!");
            connected = false;
        } else {
            // TODO poll all the axis and set them to variables

            leftstickx = gamepad.getX_LeftJoystick_Percentage();
            leftsticky = gamepad.getY_LeftJoystick_Percentage();

            rightstickx = gamepad.getX_RightJoystick_Percentage();
            rightsticky = gamepad.getY_RightJoystick_Percentage();

            if(gamepad.getControllerType() == Controller.Type.GAMEPAD) {
                righttrigger = gamepad.getZRotationPercentage();
                lefttrigger = gamepad.getZAxisPercentage();
                /*if (gamepad.getZRotationPercentage()>0) {
                    ButtonRT = true;
                } else {
                    ButtonRT = false;
                }
                if (gamepad.getZAxisPercentage()>0) {
                    ButtonLT = true;
                } else {
                    ButtonLT = false;
                }/**/
            }

            ButtonLeftStick = gamepad.getButtonValue(6);
            ButtonRightStick = gamepad.getButtonValue(7);

            if (com.scotch.OARKit.java.Controller.type.equals("Gamepad")) {
                ButtonUp = gamepad.getButtonValue(0);
                ButtonDown = gamepad.getButtonValue(1);
                ButtonLeft = gamepad.getButtonValue(2);
                ButtonRight = gamepad.getButtonValue(3);
            } else {
                float hatSwitchPosition = gamepad.getHatSwitchPosition();
                if (Float.compare(hatSwitchPosition, Component.POV.OFF) == 0) {
                    // Hat switch is not pressed. The same as Component.POV.CENTER
                    ButtonUp = false;
                    ButtonRight = false;
                    ButtonDown = false;
                    ButtonLeft = false;
                } else if (Float.compare(hatSwitchPosition, Component.POV.UP) == 0) {
                    // Do stuff when UP is pressed.
                    ButtonUp = true;
                    ButtonRight = false;
                    ButtonDown = false;
                    ButtonLeft = false;
                } else if (Float.compare(hatSwitchPosition, Component.POV.DOWN) == 0) {
                    // Do stuff when DOWN is pressed.
                    ButtonUp = false;
                    ButtonRight = false;
                    ButtonDown = true;
                    ButtonLeft = false;
                } else if (Float.compare(hatSwitchPosition, Component.POV.LEFT) == 0) {
                    // Do stuff when LEFT is pressed.
                    ButtonUp = false;
                    ButtonRight = false;
                    ButtonDown = false;
                    ButtonLeft = true;
                } else if (Float.compare(hatSwitchPosition, Component.POV.RIGHT) == 0) {
                    // Do stuff when RIGHT is pressed.
                    ButtonUp = false;
                    ButtonRight = true;
                    ButtonDown = false;
                    ButtonLeft = false;
                } else if (Float.compare(hatSwitchPosition, Component.POV.UP_LEFT) == 0) {
                    // Do stuff when UP and LEFT is pressed.
                    ButtonUp = true;
                    ButtonRight = false;
                    ButtonDown = false;
                    ButtonLeft = true;
                } else if (Float.compare(hatSwitchPosition, Component.POV.UP_RIGHT) == 0) {
                    // Do stuff when UP and RIGHT is pressed.
                    ButtonUp = true;
                    ButtonRight = true;
                    ButtonDown = false;
                    ButtonLeft = false;
                } else if (Float.compare(hatSwitchPosition, Component.POV.DOWN_LEFT) == 0) {
                    // Do stuff when DOWN and LEFT is pressed.
                    ButtonUp = false;
                    ButtonRight = false;
                    ButtonDown = true;
                    ButtonLeft = true;
                } else if (Float.compare(hatSwitchPosition, Component.POV.DOWN_RIGHT) == 0) {
                    // Do stuff when DOWN and RIGHT is pressed.
                    ButtonUp = false;
                    ButtonRight = true;
                    ButtonDown = true;
                    ButtonLeft = false;
                }
            }

            /*leftstickx = gamepad.getXAxisPercentage();
            leftsticky = gamepad.getYAxisPercentage();

            rightstickx = gamepad.getX_RightJoystick_Percentage();
            rightsticky = gamepad.getY_RightJoystick_Percentage();/**

            if (gamepad.getControllerName().contains("Xbox")&&gamepad.getControllerName().contains("360")) {
                //X, Y, A and B Buttons
                ButtonX = gamepad.getButtonValue(13);
                ButtonY = gamepad.getButtonValue(14);
                ButtonA = gamepad.getButtonValue(11);
                ButtonB = gamepad.getButtonValue(12);
                //Triggers
                if (gamepad.getZRotationPercentage()>0) {
                    ButtonRT = true;
                } else {
                    ButtonRT = false;
                }
                if (gamepad.getZAxisPercentage()>0) {
                    ButtonLT = true;
                } else {
                    ButtonLT = false;
                }
                //Bumpers
                ButtonRB = gamepad.getButtonValue(9);
                ButtonLB = gamepad.getButtonValue(8);
                //POV
                ButtonUp = gamepad.getButtonValue(0);
                ButtonDown = gamepad.getButtonValue(1);
                ButtonLeft = gamepad.getButtonValue(2);
                ButtonRight = gamepad.getButtonValue(3);
            } else {
                ButtonX = gamepad.getButtonValue(0);
                ButtonY = gamepad.getButtonValue(3);
                ButtonA = gamepad.getButtonValue(1);
                ButtonB = gamepad.getButtonValue(2);
                ButtonRT = gamepad.getButtonValue(4);
                ButtonRB = gamepad.getButtonValue(5);
                ButtonLT = gamepad.getButtonValue(6);
                ButtonLB = gamepad.getButtonValue(7);
                if (gamepad.getHatSwitchPosition() == 0.25) {
                    ButtonUp = true;
                    ButtonRight = false;
                    ButtonDown = false;
                    ButtonLeft = false;
                } else if (gamepad.getHatSwitchPosition() == 0.5) {
                    ButtonUp = false;
                    ButtonDown = true;
                    ButtonLeft = false;
                    ButtonRight = false;
                } else if (gamepad.getHatSwitchPosition() == 0.75) {
                    ButtonUp = false;
                    ButtonDown = false;
                    ButtonLeft = true;
                    ButtonRight = false;
                } else if (gamepad.getHatSwitchPosition() == 1.0) {
                    ButtonUp = false;
                    ButtonDown = false;
                    ButtonLeft = false;
                    ButtonRight = true;
                } else {
                    ButtonUp = false;
                    ButtonDown = false;
                    ButtonLeft = false;
                    ButtonRight = false;
                }
            }/**/
            createCommand();
        }
    }
    private void createCommand() {
        if (ServerConnect.connected) {
            new Interpreter("gamecontroller, " + ConvertToHex(Math.round(leftstickx)) + ConvertToHex(Math.round(leftsticky)) + ConvertToHex(Math.round(rightstickx)) + ConvertToHex(Math.round(rightsticky)) + "77000000000000").returnCommand().runCommand();
        }
    }
    private String ConvertToHex(int axis){
        return Integer.toHexString(((axis) * 15)/100);
    }
}
