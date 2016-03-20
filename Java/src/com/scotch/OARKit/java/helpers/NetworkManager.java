package com.scotch.OARKit.java.helpers;

/**
 * Created by Aren Leishman on 3/11/16.
 */

// TODO add a connection manager backend.

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.ResourceBundle;

public class NetworkManager implements Initializable {
    private float signalStrength;
    private float rawSignalStrength;
    private String OS;
    @FXML
    Stage NetWindow;
    @FXML
    Button CancelButton;
    @FXML
    Button SaveButton;
    @FXML
    TextField NameField;
    @FXML
    TextField IPField;
    @FXML
    TextField PortField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createEvents();
    }

    public void createEvents(){
        CancelButton.setOnAction(event -> {
            NetWindow = (Stage) CancelButton.getScene().getWindow();
            NetWindow.close();
        });
    }

    public NetworkManager() throws IOException {
        OS = System.getProperty("os.name");

    }

    public void update(){
        if(OS.toUpperCase().contains("X")){
            nixSignalStrength();
        } else if(OS.toUpperCase().contains("WINDOWS")){
            windowsSignalStrength();
        }
    }

    /**public void checkNetwork() throws SocketException {
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        int k=1;
        while(e.hasMoreElements()) {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements()) {
                InetAddress i = (InetAddress) ee.nextElement();
                System.out.println(i.getHostAddress());
                k=k+1;
            }
        }
        if(k==1) {
            signalStrength=-2;
        }
    }*/

    public float getRawSignalStrength(){return rawSignalStrength;}

    public float getSignalStrength() {return signalStrength;}

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
        //System.out.println(strength);
        rawSignalStrength = -100-(strength*-1);
        if(strength != 0) {
            strength =Math.abs(strength/100);
            signalStrength = strength;
        } else {
            signalStrength = -1;
        }
    }

    private void nixSignalStrength(){
        float strength = 0;
        try {
            String s = null;
            if(OS.toUpperCase().contains("MAC")){
                Process p = Runtime.getRuntime().exec("/System/Library/PrivateFrameworks/Apple80211.framework/Versions/Current/Resources/airport -I");

                BufferedReader stdInput = new BufferedReader(new
                        InputStreamReader(p.getInputStream()));

                // read the output from the command and assign the strength
                while ((s = stdInput.readLine()) != null) {
                    if (s.toLowerCase().contains("agrCtlRSSI".toLowerCase())){
                        strength = Float.parseFloat(s.substring(17));
                        rawSignalStrength = strength;
                    }
                    if (strength == 0){
                        signalStrength = -1;
                        return;
                    }
                }
            } else if(OS.toUpperCase().contains("X")){
                Process p = Runtime.getRuntime().exec("iwconfig");

                BufferedReader stdInput = new BufferedReader(new
                        InputStreamReader(p.getInputStream()));

                // read the output from the command and assign the strength
                while ((s = stdInput.readLine()) != null) {
                    if (s.toLowerCase().contains("Signal level=".toLowerCase())){
                        strength = Float.parseFloat(s.substring(43, 46));
                        rawSignalStrength = strength;
                    }
                }
                if (strength == 0){
                    signalStrength = -1;
                    return;
                }
            }
        }
        catch (IOException e) {
            System.out.println("Mac Wifi has had an error.");
            e.printStackTrace();
        }

        if(strength <= -100)
            strength = 0;
        else if(strength >= 0)
            strength = 1;
        else
            //make strength into percentage, -100 dbm being 0% and 0 dbm being 100%. Noting that the progressbar uses 0-1.
            strength = 1-(Math.abs(strength)/100);
        signalStrength = strength;
    }
}
