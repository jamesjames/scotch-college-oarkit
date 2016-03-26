package com.scotch.OARKit.java.helpers;

import com.scotch.OARKit.java.helpers.JInputJoystick;
import net.java.games.input.*;


public class gamepad {

    public static boolean connected = true;

    public float leftstickx;
    public float leftsticky;

    public float rightstickx;
    public float rightsticky;

    private int numberOfButtons;

    JInputJoystick gamepad;

    public void gamepad(){
        gamepad = new JInputJoystick(Controller.Type.GAMEPAD, Controller.Type.STICK);
        numberOfButtons = gamepad.getNumberOfButtons();

        // Check if the controller was found.
        if( !gamepad.isControllerConnected() ){
            connected = false;
            System.out.println("No controller found!");
        } else {
            System.out.println("Controller found, printing details:");
            System.out.println(gamepad.getControllerName());
            System.out.println(gamepad.getControllerType());
            System.out.println("This controller has " + numberOfButtons + " buttons");


        }
    }

    public void pollgamepad(){
        if( !gamepad.pollController() ) {
            System.out.println("Controller disconnected!");
            // Do some stuff.
        } else {
            // TODO poll all the axis and set them to variables
            leftstickx = gamepad.getX_LeftJoystick_Percentage();
            leftsticky = gamepad.getY_LeftJoystick_Percentage();

            rightstickx = gamepad.getX_RightJoystick_Percentage();
            rightsticky = gamepad.getY_RightJoystick_Percentage();
        }
    }
}
