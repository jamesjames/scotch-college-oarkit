package com.scotch.OARKit.java.Command;

/**
 * Created by campbell on 5/1/2016.
 */
public class Test extends ACommand {
    public Test() {
        super(1);
        System.out.println("It Worked!");
    }

    @Override
    public void phaseCommand() {

    }

    @Override
    public boolean checkRaw() {
        return false;
    }
}
