package com.scotch.OARKit.java.Command;



import java.util.ArrayList;

public enum BaseCommand {
    ECHO("E","Echo",new String[]{"print","echo"},1),
    BLANK("","", new String[]{},0);
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
