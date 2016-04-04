package com.scotch.OARKit.java.Command;

import com.scotch.OARKit.java.Controller;
import com.scotch.OARKit.java.helpers.Logger;
import com.scotch.OARKit.java.helpers.ServerConnect;

import java.util.Objects;

public class Commands {
    String command = BaseCommand.BLANK.command;
    String args ="";
    //FOR KNOW ARGS
    public Commands(BaseCommand command, String args){
        this.command += command.command;
        args(args);
    }
    //APPENDING ARGS LATER
    public Commands(BaseCommand command){
        this.command += command.command;
    }
    public void args(String args){
        this.args = args;
    }
    public String fetchCommand(){
        return command + args;
    }
    public void runCommand(){
        Controller.serverConnect.sendData(fetchCommand());
        if(Interpreter.validCommand&&ServerConnect.connected) {
            Logger.info("Command Sent " + command + " With args " + args);
        }
    }
}
