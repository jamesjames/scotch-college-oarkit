package com.scotch.OARKit.java.Command;

import com.scotch.OARKit.java.Controller;
import com.scotch.OARKit.java.helpers.Logger;
import com.scotch.OARKit.java.helpers.ServerConnect;

import java.util.ArrayList;
import java.util.List;

import static com.scotch.OARKit.java.Controller.serverConnect;

public class Interpreter_test {

    private static String combineArgs(String flag, String separator, List<String> args) {
        String data = "";
        String arg = "";
        for (int i = 0; i < args.size(); i++) {
            arg += args.get(i);
            arg += separator;
        }
        arg = arg.substring(0, arg.length()-1);
        data = data + flag + arg;
        //Logger.info(data);
        return data;
    }

    public static void interpret(String command) {
        String[] splitCommand = command.split(" ");
        String type = splitCommand[0];
        List<String> args = new ArrayList<>();
        for (int i = 1; i < splitCommand.length; i++) {
            args.add(splitCommand[i]);
        }

        String sendData = "";

        switch (type) {

            case "help":
                Logger.info("{E|X|M|P|G|StopServer} [arguments (separated by \" \")]");
                Logger.info("E-echo/print");
                Logger.info("X-system");
                Logger.info("M-manual");
                Logger.info("P-pi");
                Logger.info("G-GameController");
                Logger.info("the echo/print command only takes 1 argument but can handle spaces in the string to be sent");
                Logger.info("it will treat them as a single argument");
                Logger.info("all other commands will send the arguments separated from the program and each other by \"-\"");
                break;

            case "echo":
            case "print":
            case "E":
                sendData = combineArgs("E", " ", args);
                break;

            case "system":
            case "X":
                sendData = combineArgs("X", "-", args);
                break;

            case "manual":
            case "M":
                sendData = combineArgs("M", "-", args);
                break;

            case "gamecontroller":
            case "G":
                sendData = combineArgs("G", "-", args);
                break;

            case "pi":
            case "P":
                sendData = combineArgs("P", "-", args);
                break;

            case "stopserver":
                sendData = "STOPSERVER";
                break;

            case "connectcurrent":
                Controller.ServerConnect(Controller.nameLabel1.getText().replace("Name: ", ""), Controller.ipLabel1.getText().replace("IP: ", ""), Controller.portLabel1.getText().replace("Port: ", ""));
                break;

            case "connect":
                if (args.size() != 3) {
                    Logger.error("\"connect\" command requires exactly 3 arguments");
                } else if (args.size() == 3) {
                    Controller.ServerConnect(args.get(0), args.get(1), args.get(2));
                }
                break;

            case "disconnect":
                Controller.ServerDisconnect();
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
                if (sendData.equals("STOPSERVER")) {
                    Controller.ServerDisconnect();
                }
            }
        }
    }
}