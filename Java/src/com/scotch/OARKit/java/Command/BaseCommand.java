package com.scotch.OARKit.java.Command;

import java.util.ArrayList;

public enum BaseCommand {
    ECHO("E"),
    BLANK("");
    public String command;
    BaseCommand(String command){
        this.command = command;
    }
}
