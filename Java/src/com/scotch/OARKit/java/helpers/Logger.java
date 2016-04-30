package com.scotch.OARKit.java.helpers;


import com.scotch.OARKit.java.Main;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by CAMPBELL MILLAR/ZENOTON STUDIOS on 9/03/2015. ALL LIBRARIES ARE LICENCED UNDER GNU LGPL
 */
public class Logger {
    //TODO Move to a JavaFX
    public static void info(Object object) {
        String timeStamp = new SimpleDateFormat("[HH:mm:ss] ").format(Calendar.getInstance().getTime());
        System.out.println("[INFO] " + timeStamp + object);
    }

    public static void error(Object object) {
        String timeStamp = new SimpleDateFormat("[HH:mm:ss] ").format(Calendar.getInstance().getTime());
        System.err.println("[ERROR] " + timeStamp + object);
    }

    public static void debug(Object object) {
        if (Main.DevMode.equals("true")&&false) {
            String timeStamp = new SimpleDateFormat("[HH:mm:ss] ").format(Calendar.getInstance().getTime());
            System.out.println("[DEBUG] " + timeStamp + object);
        }
    }

    public static void warn(Object object) {
        String timeStamp = new SimpleDateFormat("[HH:mm:ss] ").format(Calendar.getInstance().getTime());
        System.err.println("[WARNING] " + timeStamp + object);
    }

}