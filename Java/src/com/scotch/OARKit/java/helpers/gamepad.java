package com.scotch.OARKit.java.helpers;

import com.kenai.jaffl.struct.Struct;
import com.scotch.OARKit.java.*;
import com.scotch.OARKit.java.Command.Interpreter;
import com.scotch.OARKit.java.Command.Interpreter_test;
import com.scotch.OARKit.java.helpers.JInputJoystick;
import javafx.scene.control.Button;
import net.java.games.input.*;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

import java.util.Objects;

import static com.scotch.OARKit.java.helpers.NativeLoader.loadLib;


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

        // Check if the controller was found.
        if(gamepad.isControllerConnected()/*&&gamepad.getControllerType().equals("Gamepad")/**/){
            numberOfButtons = gamepad.getNumberOfButtons();
            Logger.info("Controller found, printing details:");
            Logger.info(gamepad.getControllerName());
            Logger.info(gamepad.getControllerType());
            Logger.info("This controller has " + numberOfButtons + " buttons");
            Logger.info("Complete!");
        } else {
            connected = false;
            Logger.info("No controller found!");
        }
    }

    private void hatSwitch(){
        float hatSwitchPosition = gamepad.getHatSwitchPosition();
        ButtonUp = (Float.compare(hatSwitchPosition, Component.POV.UP) == 0)||(Float.compare(hatSwitchPosition, Component.POV.UP_LEFT) == 0)||(Float.compare(hatSwitchPosition, Component.POV.UP_RIGHT) == 0);
        ButtonRight = (Float.compare(hatSwitchPosition, Component.POV.RIGHT) == 0)||(Float.compare(hatSwitchPosition, Component.POV.UP_RIGHT) == 0)||(Float.compare(hatSwitchPosition, Component.POV.DOWN_RIGHT) == 0);
        ButtonDown = (Float.compare(hatSwitchPosition, Component.POV.DOWN) == 0)||(Float.compare(hatSwitchPosition, Component.POV.DOWN_LEFT) == 0)||(Float.compare(hatSwitchPosition, Component.POV.DOWN_RIGHT) == 0);
        ButtonLeft = (Float.compare(hatSwitchPosition, Component.POV.LEFT) == 0)||(Float.compare(hatSwitchPosition, Component.POV.UP_LEFT) == 0)||(Float.compare(hatSwitchPosition, Component.POV.DOWN_LEFT) == 0);
    }

    public void pollgamepad(){
        if(!gamepad.pollController()) {
            Logger.info("Controller disconnected!");
            connected = false;
        } else if(gamepad.pollController()){

            leftstickx = gamepad.getX_LeftJoystick_Percentage();
            leftsticky = gamepad.getY_LeftJoystick_Percentage();

            rightstickx = gamepad.getX_RightJoystick_Percentage();
            rightsticky = gamepad.getY_RightJoystick_Percentage();

            if (Main.os.contains("win")) {
                try {
                    if (gamepad.getControllerType() == Controller.Type.GAMEPAD) {
                        //X, Y, A and B Buttons
                        ButtonX = gamepad.getButtonValue(2);
                        ButtonY = gamepad.getButtonValue(3);
                        ButtonA = gamepad.getButtonValue(0);
                        ButtonB = gamepad.getButtonValue(1);
                        //POV
                        hatSwitch();
                        //Stick Buttons
                        ButtonLeftStick = gamepad.getButtonValue(8);
                        ButtonRightStick = gamepad.getButtonValue(9);
                        //Bumpers
                        ButtonRB = gamepad.getButtonValue(5);
                        ButtonLB = gamepad.getButtonValue(4);
                        //Triggers
                        float triggeraxis = gamepad.getZAxisPercentage()-50;
                        if (triggeraxis < 0) {
                            righttrigger = -triggeraxis*2;
                            lefttrigger = 0;
                        } else if (triggeraxis > 0) {
                            righttrigger = 0;
                            lefttrigger = triggeraxis*2;
                        } else {
                            righttrigger = 0;
                            lefttrigger  = 0;
                        }
                    } else if (gamepad.getControllerType() == Controller.Type.STICK) {
                        //X, Y, A and B Buttons
                        ButtonX = gamepad.getButtonValue(1);
                        ButtonY = gamepad.getButtonValue(4);
                        ButtonA = gamepad.getButtonValue(2);
                        ButtonB = gamepad.getButtonValue(3);
                        //POV
                        hatSwitch();
                        //Stick Buttons
                        ButtonLeftStick = gamepad.getButtonValue(11);
                        ButtonRightStick = gamepad.getButtonValue(12);
                        //Bumpers
                        ButtonRB = gamepad.getButtonValue(6);
                        ButtonLB = gamepad.getButtonValue(5);
                        //Triggers
                        ButtonRT = gamepad.getButtonValue(8);
                        ButtonLT = gamepad.getButtonValue(7);
                    }
                } catch (IndexOutOfBoundsException e) {
                    Logger.info("This controller is not compatible. Please try another one.");
                    connected = false;
                }
            } else if (Main.os.contains("mac")) {
                try {
                    if (gamepad.getControllerType() == Controller.Type.GAMEPAD) {
                        //X, Y, A and B Buttons
                        ButtonX = gamepad.getButtonValue(13);
                        ButtonY = gamepad.getButtonValue(14);
                        ButtonA = gamepad.getButtonValue(11);
                        ButtonB = gamepad.getButtonValue(12);
                        //POV
                        ButtonUp = gamepad.getButtonValue(0);
                        ButtonDown = gamepad.getButtonValue(1);
                        ButtonLeft = gamepad.getButtonValue(2);
                        ButtonRight = gamepad.getButtonValue(3);
                        //Stick Buttons
                        ButtonLeftStick = gamepad.getButtonValue(6);
                        ButtonRightStick = gamepad.getButtonValue(7);
                        //Bumpers
                        ButtonRB = gamepad.getButtonValue(9);
                        ButtonLB = gamepad.getButtonValue(8);
                        //Triggers
                        righttrigger = gamepad.getZRotationPercentage();
                        lefttrigger = gamepad.getZAxisPercentage();
                    } else if (gamepad.getControllerType() == Controller.Type.STICK) {
                        //X, Y, A and B Buttons
                        ButtonX = gamepad.getButtonValue(0);
                        ButtonY = gamepad.getButtonValue(3);
                        ButtonA = gamepad.getButtonValue(1);
                        ButtonB = gamepad.getButtonValue(2);
                        //POV
                        hatSwitch();
                        //Stick Buttons
                        ButtonLeftStick = gamepad.getButtonValue(10);
                        ButtonRightStick = gamepad.getButtonValue(11);
                        //Bumpers
                        ButtonRB = gamepad.getButtonValue(5);
                        ButtonLB = gamepad.getButtonValue(4);
                        //Triggers
                        ButtonRT = gamepad.getButtonValue(7);
                        ButtonLT = gamepad.getButtonValue(6);
                    }
                } catch (IndexOutOfBoundsException e) {
                    Logger.info("This controller is not compatible. Please try another one.");
                    connected = false;
                }
            }
            createCommand();
        }
    }
    private void createCommand() {
        if (ServerConnect.connected) {
            //new Interpreter("gamecontroller " + ConvertToHex(Math.round(leftstickx)) + ConvertToHex(Math.round(leftsticky)) + ConvertToHex(Math.round(rightstickx)) + ConvertToHex(Math.round(rightsticky)) + "77000000000000").returnCommand().runCommand();
            Interpreter_test.interpret("G " + ConvertToHex(Math.round(leftstickx)) + ConvertToHex(Math.round(leftsticky)) + ConvertToHex(Math.round(rightstickx)) + ConvertToHex(Math.round(rightsticky)) + "77000000000000");
        }
    }
    private String ConvertToHex(int axis){
        return Integer.toHexString(((axis) * 15)/100);
    }
}
