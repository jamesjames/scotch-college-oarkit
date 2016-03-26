package com.scotch.OARKit.java.Command;



import java.util.ArrayList;

public enum BaseCommand {
    ECHO("E","Echo",new String[]{"print","echo"},1),
    BLANK("","", new String[]{},0),
    PI("P","Pi", new String[]{"Pi"},1),
    STOPSERVER("STOPSERVER","Stop Server", new String[]{"stopserver"},0), //interpreter cannot handle multi-character Commands Apparently.
    GAMECONTROLLER("G","gamecontroller", new String[]{"gamecontroller"},1);

    public String command;
    public String[] alias;
    public int args;
    public String localizedName;
    BaseCommand(String command, String localizedName, String[] alias, int args){
        this.command = command;
        this.alias = alias;
        this.args = args;
        this.localizedName = localizedName;
    }
}
