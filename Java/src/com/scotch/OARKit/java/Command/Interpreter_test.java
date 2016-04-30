package com.scotch.OARKit.java.Command;

import com.scotch.OARKit.java.Controller;
import com.scotch.OARKit.java.helpers.Logger;
import com.scotch.OARKit.java.helpers.ServerConnect;

import java.util.ArrayList;
import java.util.List;

import static com.scotch.OARKit.java.Controller.serverConnect;

public class Interpreter_test {

    private static String combineArgs(String flag, String seporater, List<String> args) {
        String data;
        String arg = "";
        for (int i = 0; i < args.size(); i++) {
            arg += args.get(i);
            arg += seporater;
        }
        arg = arg.substring(0, arg.length()-1);
        data = flag + arg;
        //Logger.info(data);
        return data;
    }

    public static void interpreter(String command) {
        String[] splitCommand = command.split(" ");
        String type = splitCommand[0].toLowerCase();
        List<String> args = new ArrayList<>();
        for (int i = 1; i < splitCommand.length; i++) {
            args.add(splitCommand[i]);
        }

        String sendData = "";
        switch (type) {

            case "help":
                Logger.info("need to add this");
                break;

            case "print":
                sendData = combineArgs("E", " ", args);
                break;

            case "system":
                sendData = combineArgs("X", "-", args);
                break;

            case "manual":
                sendData = combineArgs("M", "-", args);
                break;

            case "stopserver":
                sendData = "STOPSERVER";
                break;

            case "gamecontroller":
                sendData = combineArgs("G", "-", args);
                break;

            case "pi":
                sendData = combineArgs("P", "-", args);
                break;

            default:
                Logger.error("COMMAND NOT FOUND!");
                break;
        }

        if (!sendData.isEmpty()) {
            if (!ServerConnect.connected) {
                Logger.error("Socket is not connected");
            } else if (ServerConnect.connected) {
                Logger.info("Sent " + sendData);
                serverConnect.sendData(sendData);
            }
        }
    }
}

