package com.scotch.OARKit.java.Command;

/**
 * Created by Campbell Millar on 11/03/2016.
 */
public abstract class ACommand {
    public final int argsLength;
    public String FinalString;

    ACommand(int argsLength) {
        this.argsLength = argsLength;
    }

    public abstract void phaseCommand();

    public void runString() {

    }

    public void checkArgs() {

    }
}
