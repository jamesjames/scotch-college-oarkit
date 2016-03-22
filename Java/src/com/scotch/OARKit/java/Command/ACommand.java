package com.scotch.OARKit.java.Command;

/**
 * Created by Campbell Millar on 11/03/2016.
 */
public abstract class ACommand {
    public String FinalString;
    public final int argsLength;
    ACommand(int argsLength){
        this.argsLength = argsLength;
    }
    public abstract void phaseCommand();
    public void runString(){
        
    }
    public void checkArgs(){

    }
}
