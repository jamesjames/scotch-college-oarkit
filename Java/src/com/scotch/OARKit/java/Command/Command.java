package com.scotch.OARKit.java.Command;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Campbell Millar on 5/1/2016.
 *
 * A object to be sent over the line to the server
 */
public class Command implements Serializable{

    @Override
    public String toString() {
        return "Command{" +
                "arguments=" + arguments +
                ", commandBase='" + commandBase + '\'' +
                '}';
    }

    private static final long serialVersionUID = -4007064073577677293L;
    public List<String> arguments;
    public String commandBase;

    public Command(String commandBase, String[] arguments){
        this.commandBase = commandBase;
        this.arguments = new ArrayList<>();
        Collections.addAll(this.arguments, arguments);
    }


}
