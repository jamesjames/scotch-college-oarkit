package com.scotch.OARKit.java.Command;

import java.util.ArrayList;

/**
 * Created by Campbell Millar on 7/03/2016.
 */
public enum BaseCommand {
    ECHO("E"),
    BLANK("");
    public String command;
    BaseCommand(String command){
        this.command = command;
    }
}
