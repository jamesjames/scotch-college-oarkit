package com.scotch.OARKit.java.Command;

/**
 * Created by Campbell Millar on 22/03/2016.
 */
public class Echo extends ACommand {
    public Echo() {
        super(1);
    }

    @Override
    public void phaseCommand() {

    }

    @Override
    public boolean checkRaw() {
        return false;
    }
}
