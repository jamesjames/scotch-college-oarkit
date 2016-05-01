package com.scotch.OARKit.java.Command;

import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Campbell Millar on 11/03/2016.
 * A Class to be a template/master class for all command based things - only back end
 * It also finds all classes that extend this class inside com.scotch.OARKit.java.Command and auto inits them and save them to a array for later possessing
 */
public abstract class ACommand {
    public final int argsLength;

    public static List<ACommand> objectList = new ArrayList<ACommand>();
    ACommand(int argsLength) {
        this.argsLength = argsLength;
    }

    public abstract void phaseCommand();

    public abstract boolean checkRaw();

    public void sendCommand(){
        for
    }
    public static void findClassesAndInit(){
        Reflections reflections = new Reflections("com.scotch.OARKit.java.Command");
        Set<? extends ACommand> classes = reflections.getSubTypesOf((Class)ACommand.class);
        Class[] classesArray = classes.toArray(new Class[classes.size()]);
        for (Class c: classesArray) {
            try {
                objectList.add((ACommand)c.getConstructor().newInstance());
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
