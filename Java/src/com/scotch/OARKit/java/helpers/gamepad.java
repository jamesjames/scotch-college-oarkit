package com.scotch.OARKit.java.helpers;

import com.scotch.OARKit.java.helpers.JInputJoystick;
import net.java.games.input.Controller;

/**
 * Created by Aren Leishman on 3/18/16.
 */
public class gamepad {

    public static boolean connected = true;

    public int leftstickx;
    public int leftsticky;

    public int rightstickx;
    public int rightsticky;

    private int numberOfButtons;

    JInputJoystick gamepad;

    public void gamepad(){
        gamepad = new JInputJoystick(Controller.Type.STICK, Controller.Type.GAMEPAD);
        numberOfButtons = gamepad.getNumberOfButtons();

        // Check if the controller was found.
        if( !gamepad.isControllerConnected() ){
            connected = false;
            System.out.println("No controller found!");
        } else {
            System.out.println("Controller found");
            System.out.println("This controller has " + numberOfButtons + " buttons");
        }
    }

    public void pollgamepad(){
        if( !gamepad.pollController() ) {
            System.out.println("Controller disconnected!");
            // Do some stuff.
        } else {
            // TODO poll all the axis and set them to variables
            System.out.println(gamepad.getXAxisPercentage());
        }
    }
}
