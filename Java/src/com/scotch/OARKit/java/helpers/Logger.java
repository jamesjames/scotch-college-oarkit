package com.scotch.OARKit.java.helpers;


import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by CAMPBELL MILLAR/ZENOTON STUDIOS on 9/03/2015. ALL LIBRARIES ARE LICENCED UNDER GNU LGPL
 */
public class Logger {
    //TODO Move to a JavaFX
    public static void info(Object object) {
        String timeStamp = new SimpleDateFormat("[HH:mm:ss] ").format(Calendar.getInstance().getTime());
        System.out.println((char) 27 + "[info] " + timeStamp + object);
    }

    public static void error(Object object) {
        String timeStamp = new SimpleDateFormat("[HH:mm:ss] ").format(Calendar.getInstance().getTime());
        System.err.println((char) 27 + "[error] " + timeStamp + object);
    }

    public static void debug(Object object) {
        String timeStamp = new SimpleDateFormat("[HH:mm:ss] ").format(Calendar.getInstance().getTime());
        System.out.println((char) 27 + "[debug] " + timeStamp + object);
    }

    public static void warn(Object object) {
        String timeStamp = new SimpleDateFormat("[HH:mm:ss] ").format(Calendar.getInstance().getTime());
        System.err.println((char) 27 + "[warning] " + timeStamp + object);
    }

}