package com.scotch.OARKit.java.helpers;

import com.kenai.jaffl.struct.Struct;
import com.scotch.OARKit.java.Command.Interpreter;
import com.scotch.OARKit.java.helpers.JInputJoystick;
import javafx.scene.control.Button;
import net.java.games.input.*;


public class gamepad {

    public static boolean connected = true;

    public float leftstickx;
    public float leftsticky;

    public float rightstickx;
    public float rightsticky;

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

    private int numberOfButtons;

    private JInputJoystick gamepad;

    public void gamepad(){
        gamepad = new JInputJoystick(Controller.Type.GAMEPAD, Controller.Type.STICK);
        gamepad.pollController();
        numberOfButtons = gamepad.getNumberOfButtons();

        // Check if the controller was found.
        if(gamepad.isControllerConnected()&&gamepad.getControllerType().equals("Gamepad")){
            Logger.info("-----------------------------------------");
            Logger.info("Controller found, printing details:");
            Logger.info(gamepad.getControllerName());
            Logger.info(gamepad.getControllerType());
            Logger.info("This controller has " + numberOfButtons + " buttons");
            Logger.info("-----------------------------------------");
        } else if (gamepad.isControllerConnected()&&gamepad.getControllerType().equals("Stick")){
            connected = false;
            Logger.info("-------------------------------------------------------------");
            Logger.info("Found joystick. Joysticks not supported. Please use a gamepad");
            Logger.info("-------------------------------------------------------------");
        } else {
            connected = false;
            Logger.info("--------------------");
            Logger.info("No controller found!");
            Logger.info("--------------------");
        }
    }

    public void pollgamepad(){
        if(!gamepad.pollController()) {
            Logger.info("Controller disconnected!");
            connected = false;
        } else {
            // TODO poll all the axis and set them to variables

            leftstickx = gamepad.getXAxisPercentage();
            leftsticky = gamepad.getYAxisPercentage();

            rightstickx = gamepad.getXRotationPercentage();
            rightsticky = gamepad.getYRotationPercentage();

            if ((gamepad.getControllerName().contains("Xbox")&&gamepad.getControllerName().contains("360"))||gamepad.getControllerName().contains("3D")) {
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
            }
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
