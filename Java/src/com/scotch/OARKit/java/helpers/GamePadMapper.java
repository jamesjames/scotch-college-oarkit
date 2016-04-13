package com.scotch.OARKit.java.helpers;

import net.java.games.input.Controller;
import com.scotch.OARKit.java.helpers.JInputJoystick;

//TODO add the rest of the buttons and outputting to a file

public class GamePadMapper extends Thread {

    public JInputJoystick GamePad;

    @Override
    public void run() {
        GamePad = new JInputJoystick(Controller.Type.GAMEPAD, Controller.Type.STICK);
        boolean set;
        try {
            Logger.info("Set Button A");
            set = false;
            while (!set) {
                for (int i = 0; i < GamePad.getNumberOfButtons(); i++) {
                    if (GamePad.getButtonValue(i)) {
                        Logger.info("Button A assigned to: "+i);
                        set = true;
                    }
                }
                sleep(10);
            }
            sleep(100);
            Logger.info("Set Button B");
            set = false;
            while (!set) {
                for (int i = 0; i < GamePad.getNumberOfButtons(); i++) {
                    if (GamePad.getButtonValue(i)) {
                        Logger.info("Button B assigned to: "+i);
                        set = true;
                    }
                }
                sleep(10);
            }
            sleep(100);
            Logger.info("Set Button X");
            set = false;
            while (!set) {
                for (int i = 0; i < GamePad.getNumberOfButtons(); i++) {
                    if (GamePad.getButtonValue(i)) {
                        Logger.info("Button X assigned to: "+i);
                        set = true;
                    }
                }
                sleep(10);
            }
            sleep(100);
            Logger.info("Set Button Y");
            set = false;
            while (!set) {
                for (int i = 0; i < GamePad.getNumberOfButtons(); i++) {
                    if (GamePad.getButtonValue(i)) {
                        Logger.info("Button Y assigned to: "+i);
                        set = true;
                    }
                }
                sleep(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
