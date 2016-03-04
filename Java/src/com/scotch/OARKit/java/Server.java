package com.scotch.OARKit.java;

import org.python.util.PythonInterpreter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Campbell Millar on 4/03/2016.
 */
public class Server {
    Server() throws IOException {
        PythonInterpreter interp = new PythonInterpreter();
        interp.exec("import sys");
        //ALLOWS US TO PASS ARGS
        //interp.exec("sys.argv = ['debug']");
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("com/scotch/OARKit/assets/properties/default.properties");
        Properties p = new Properties();
        p.load(inputStream);
        interp.execfile(getClass().getClassLoader().getResource(p.getProperty("serverLoc")).openStream());
    }
}
