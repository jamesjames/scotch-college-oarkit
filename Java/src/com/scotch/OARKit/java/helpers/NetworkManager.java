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
    private float signalStrength;
    private String OS;
    public NetworkManager(){
        OS = System.getProperty("os.name");

    }
    public void update(){
        if(OS.toUpperCase().contains("MAC")){
            macSignalStrength();
        } else if(OS.toUpperCase().contains("WINDOWS")){
            windowsSignalStrength();
        } else if(OS.toUpperCase().contains("NIX")){

        }
    }

    public float getSignalStrength() {
        return signalStrength;
    }

    private void windowsSignalStrength(){
        float strength = 0;

        try {
            String s = null;
            Process p = Runtime.getRuntime().exec("netsh wlan show interfaces");

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));
            // read the output from the command and assign the strength
            while ((s = stdInput.readLine()) != null) {
                if (s.toLowerCase().contains("Signal".toLowerCase())){
                    strength = Float.parseFloat(s.substring(29).replace("%",""));
                }
            }
        }

        catch (IOException e) {
            System.out.println("Windows Wifi has had an error.");
            e.printStackTrace();
        }
        System.out.println(strength);
        if(strength != 0) {
            strength =Math.abs(strength/100);
            signalStrength = strength;
        } else {
            System.out.println("Not on WiFi");
        }
    }
    private void macSignalStrength(){
        float strength = 0;

        try {
            String s = null;
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

        if(strength <= -100)
            strength = 0;
        else if(strength >= -50)
            strength = 100;
        else
            strength = 2 * (strength + 100);
        signalStrength = strength;
    }
}
