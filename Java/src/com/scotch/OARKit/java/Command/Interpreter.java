package com.scotch.OARKit.java.Command;

import com.scotch.OARKit.java.Controller;
import com.scotch.OARKit.java.helpers.Logger;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

import static com.scotch.OARKit.java.Controller.serverConnect;
import static org.python.core.PySystemState.platform;

public class Interpreter {
    public static BaseCommand baseCommand = BaseCommand.BLANK;
    public static Boolean validCommand;
    String command = "";
    //TODO IMPLEMENT A LIST FOR ARGS
    //List<String> args = new ArrayList<String>();
    String args="";
    public Interpreter(String command){
        this.command = command;
        phaseBase();

    }
    private void phaseBase(){
        baseCommand = BaseCommand.BLANK;
        String stringBase = command.split(" ")[0];
        for( int i = 0; i < BaseCommand.values().length; i++){
            for( int t = 0; t < BaseCommand.values()[i].alias.length; t++){
                //Logger.info(BaseCommand.values()[i].alias[t]);
                if(BaseCommand.values()[i].alias[t].equals(stringBase.toLowerCase())){
                    baseCommand = BaseCommand.values()[i];
                    phaseArgs();
                }
            }
        }
        validCommand = !(baseCommand == BaseCommand.BLANK);
        Logger.debug(validCommand);
        Logger.debug(baseCommand == BaseCommand.BLANK);
        if(baseCommand == BaseCommand.BLANK){
            Logger.error("COMMAND NOT FOUND :O");
        }
        if(baseCommand == BaseCommand.STOPSERVER){
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            Logger.info("Closing Socket");
                            serverConnect.socketClose();
                            Controller.disconnectServer();
                        }
                    },
                    50
            );
        }
    }
    private void phaseArgs(){
        if (command.split(" ").length-1 != baseCommand.args){
            Logger.error("NOT SUFFICIENT ARGS, KILLING REQUEST");
            this.args = "";
            this.baseCommand = BaseCommand.BLANK;
        } else if (baseCommand.args != 0){
            String stringArgs = command.split(" ",2)[1];
            this.args = stringArgs;
        } else {
            this.args = "";
        }
    }
    public Commands returnCommand(){
        return new Commands(baseCommand,args);
    }
}
