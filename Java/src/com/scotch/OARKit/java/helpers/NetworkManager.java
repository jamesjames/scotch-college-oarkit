package com.scotch.OARKit.java.helpers;

/**
 * Created by Aren Leishman on 3/11/16.
 */

// TODO add a connection manager window
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.*;

public class NetworkManager {

    public static float signalStrength;

    public static float getStrength(){
        switch(System.getProperty("os.name")){
            case "Mac OS X":
                signalStrength = macSignalStrength();
        }
        System.out.println(signalStrength);
        return signalStrength;
    }

    private static float macSignalStrength(){
        float strength = 0;
        String s = null;
        try {
            Process p = Runtime.getRuntime().exec("/System/Library/PrivateFrameworks/Apple80211.framework/Versions/Current/Resources/airport -I");

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            // read the output from the command and assign the strength
            while ((s = stdInput.readLine()) != null) {
                if (s.toLowerCase().contains("agrCtlRSSI".toLowerCase())){
                    strength = Float.parseFloat(s.substring(17));
                }
            }
        }
        catch (IOException e) {
            System.out.println("Mac Wifi has had an error.");
            e.printStackTrace();
        }

        strength = (strength/100)*-1;
        return strength;
    }
}
