package com.scotch.OARKit.java.helpers;

import com.scotch.OARKit.java.Controller;
import com.scotch.OARKit.java.ServerList.GetServerList;
import com.scotch.OARKit.java.ServerList.ServerList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class NetworkManager implements Initializable {
    private String ConnectionType;
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
    Button DeleteButton;
    @FXML
    TextField NameField;
    @FXML
    TextField IPField;
    @FXML
    TextField PortField;
    @FXML
    MenuButton configSelector;

    public void AddConfigToList() {
        configSelector.getItems().clear();
        String[] servers = ServerList.getKeys();

        for (int i = 0; i < servers.length; i++) {
            //System.out.println(servers[i]);
            MenuItem newServerName =new MenuItem(servers[i]);
            newServerName.setId(servers[i]);
            final int finalI = i;
            newServerName.setOnAction(event -> {
                String ip = ServerList.getIPAndPort(servers[finalI])[0];
                String port = ServerList.getIPAndPort(servers[finalI])[1];
                //System.out.println(servers[finalI]+", "+ip+", "+port);
                NameField.setText(servers[finalI]);
                IPField.setText(ip);
                PortField.setText(port);
            });
            Platform.runLater(() -> configSelector.getItems().addAll(newServerName));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createEvents();
        AddConfigToList();
    }

    public void createEvents(){

        DeleteButton.setOnAction(event -> {
            //TODO add code to delete entry in server list
            System.out.println("Connection '"+NameField.getText()+"' Deleted");
            IPField.setText("");
            PortField.setText("");
            NameField.setText("");
        });/**/

        CancelButton.setOnAction(event -> {
            NetWindow = (Stage) CancelButton.getScene().getWindow();
            NetWindow.close();
        });

        SaveButton.setOnAction(event -> {
            if (NameField.getText().isEmpty()||!NameField.getText().matches("[a-zA-Z0-9 ]*")) {
                try {
                    Stage NetWindow = new Stage();
                    Parent NetRoot = FXMLLoader.load(getClass().getClassLoader().getResource("com/scotch/OARKit/java/NetworkManagerErrors/NetworkManagerErrorMessageName.fxml"));
                    NetWindow.setTitle("Name Error");
                    NetWindow.setScene(new Scene(NetRoot, 224, 124));
                    NetWindow.setAlwaysOnTop(true);
                    NetWindow.setResizable(false);
                    NetWindow.initModality(Modality.WINDOW_MODAL);
                    NetWindow.initOwner(SaveButton.getScene().getWindow());
                    NetWindow.show();
                } catch (IOException e){
                    e.printStackTrace();
                }
            } else if (IPField.getText().isEmpty()||!IPField.getText().matches("^(?:(?:25[0-5]|2[0-4]\\d|1\\d\\d|\\d\\d|[1-9])\\.)(?:(?:25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){2}(?:25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$")) {
                try {
                    Stage NetWindow = new Stage();
                    Parent NetRoot = FXMLLoader.load(getClass().getClassLoader().getResource("com/scotch/OARKit/java/NetworkManagerErrors/NetworkManagerErrorMessageIP.fxml"));
                    NetWindow.setTitle("IP Error");
                    NetWindow.setScene(new Scene(NetRoot, 212, 117));
                    NetWindow.setAlwaysOnTop(true);
                    NetWindow.setResizable(false);
                    NetWindow.initModality(Modality.WINDOW_MODAL);
                    NetWindow.initOwner(SaveButton.getScene().getWindow());
                    NetWindow.show();
                } catch (IOException e){
                    e.printStackTrace();
                }
            } else if (PortField.getText().isEmpty()||!PortField.getText().matches("\\d{4}")) {
                try {
                    Stage NetWindow = new Stage();
                    Parent NetRoot = FXMLLoader.load(getClass().getClassLoader().getResource("com/scotch/OARKit/java/NetworkManagerErrors/NetworkManagerErrorMessagePort.fxml"));
                    NetWindow.setTitle("Port Error");
                    NetWindow.setScene(new Scene(NetRoot, 200, 103));
                    NetWindow.setAlwaysOnTop(true);
                    NetWindow.setResizable(false);
                    NetWindow.initModality(Modality.WINDOW_MODAL);
                    NetWindow.initOwner(SaveButton.getScene().getWindow());
                    NetWindow.show();
                } catch (IOException e){
                    e.printStackTrace();
                }
            } else {
                String[] serverConfig = new String[2];
                serverConfig[0] = IPField.getText();
                serverConfig[1] = PortField.getText();
                GetServerList.saveServer(NameField.getText(), serverConfig);
                NetWindow = (Stage) SaveButton.getScene().getWindow();
                NetWindow.close();
                Controller.AddConfigToList();
            }
        });
    }

    public NetworkManager() throws IOException{
        OS = System.getProperty("os.name");
    }

    public void update() throws SocketException {
        if(checkNetwork()) {
            if (OS.toUpperCase().contains("X")) {
                if(nixSignalStrength()){
                    ConnectionType = "Wifi";
                } else {
                    ConnectionType = "Ethernet";

                }
            } else if (OS.toUpperCase().contains("WINDOWS")) {
                if(windowsSignalStrength()){
                    ConnectionType = "Wifi";
                } else {
                    ConnectionType = "Ethernet";
                }
            }
        }
    }

    public boolean checkNetwork() throws SocketException {
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        int k=0;
        while(e.hasMoreElements()) {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements()) {
                InetAddress i = (InetAddress) ee.nextElement();
                String f = i.toString();
                if(f.contains(".")){
                    k++;
                }
            }
        }
        if(k==1) {
            ConnectionType = "Not Connected";
            return false;
        }
        else{
            return true;
        }
    }

    public float getRawSignalStrength(){return rawSignalStrength;}

    public float getSignalStrength() {return signalStrength;}

    public String getConnectionType() {return ConnectionType;}

    private boolean windowsSignalStrength(){
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
            return true;
        } else {
            signalStrength = -1;
            return false;
        }
    }

    private boolean nixSignalStrength(){
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
                        return false;
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
                    return false;
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
        return true;
    }
}
