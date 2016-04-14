package com.scotch.OARKit.java.helpers;

import net.java.games.input.Controller;

import java.util.ArrayList;

//TODO add the rest of the buttons and outputting to a file

public class GamePadMapper extends Thread {

    private gamepad GamePad;

    public ArrayList<Object> map = new ArrayList<>();

    @Override
    public void run() {
        JInputJoystick gamepad = new JInputJoystick(Controller.Type.GAMEPAD, Controller.Type.STICK);
        map.add(gamepad.getControllerName());
        //Logger.info(GamePad.numberOfButtons);
        //Logger.info(gamepad.getButtonsValues().size());
        map.add(GamePad.numberOfButtons);
        boolean set;
        try {
            Logger.info("Set Button X");
            set = false;
            while (!set) {
                for (int i = 0; i < GamePad.numberOfButtons; i++) {
                    //Logger.info("Size: "+gamepad.getButtonsValues().size());
                    //Logger.info("Index: "+i);
                    if (gamepad.getButtonValue(i)) {
                        Logger.info("Button X assigned to: "+i);
                        map.add(i);
                        set = true;
                    }
                }
                sleep(100);
            }
            sleep(100);
            Logger.info("Set Button Y");
            set = false;
            while (!set) {
                for (int i = 0; i < GamePad.numberOfButtons; i++) {
                    if (gamepad.getButtonValue(i)) {
                        Logger.info("Button Y assigned to: "+i);
                        map.add(i);
                        set = true;
                    }
                }
                sleep(100);
            }
            sleep(100);
            Logger.info("Set Button A");
            set = false;
            while (!set) {
                for (int i = 0; i < GamePad.numberOfButtons; i++) {
                    if (gamepad.getButtonValue(i)) {
                        Logger.info("Button A assigned to: "+i);
                        map.add(i);
                        set = true;
                    }
                }
                sleep(100);
            }
            sleep(100);
            Logger.info("Set Button B");
            set = false;
            while (!set) {
                for (int i = 0; i < GamePad.numberOfButtons; i++) {
                    if (gamepad.getButtonValue(i)) {
                        Logger.info("Button B assigned to: "+i);
                        map.add(i);
                        set = true;
                    }
                }
                sleep(100);
            }
            Logger.info("Finished Mapping Controller");
            Logger.info(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
