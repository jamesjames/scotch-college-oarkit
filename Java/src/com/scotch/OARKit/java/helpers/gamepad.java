package com.scotch.OARKit.java.helpers;

import net.java.games.input.Controller;

public class gamepad {
    public void gamepad(){
        JInputJoystick joystick = new JInputJoystick(Controller.Type.STICK, Controller.Type.GAMEPAD);
    }
}
