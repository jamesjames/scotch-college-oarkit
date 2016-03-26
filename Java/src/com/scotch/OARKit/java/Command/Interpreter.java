package com.scotch.OARKit.java.Command;

import java.util.ArrayList;
import java.util.List;

public class Interpreter {
    BaseCommand baseCommand = BaseCommand.BLANK;
    String command = "";
    //TODO IMPLEMENT A LIST FOR ARGS
    //List<String> args = new ArrayList<String>();
    String args="";
    public Interpreter(String command){
        this.command = command;
        phaseBase();

    }
    private void phaseBase(){
        String stringBase = command.split(" ")[0];
        for( int i = 0; i < BaseCommand.values().length; i++){
            for( int t = 0; t < BaseCommand.values()[i].alias.length; t++){
                //System.out.println(BaseCommand.values()[i].alias[t]);
                if(BaseCommand.values()[i].alias[t].equals(stringBase.toLowerCase())){
                    baseCommand = BaseCommand.values()[i];
                    phaseArgs();
                }
            }
        }
        if(baseCommand == BaseCommand.BLANK){
            System.err.println("ERROR - COMMAND NOT FOUND :O");
        }
    }
    private void phaseArgs(){
        if (command.split(" ").length-1 != baseCommand.args){
            System.err.println("ERROR NOT SUFFICIENT ARGS, KILLING REQUEST");
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
