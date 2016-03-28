package com.scotch.OARKit.java.helpers;



// TODO add a connection manager backend.

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createEvents();
    }

    public void createEvents(){

        File folder = new File("com/scotch/OARKit/assets/servers");
        File[] listOfFiles = folder.listFiles();
        List<String> servers = new ArrayList<>();

        for (int i = 0; i < listOfFiles.length; i++) {
            //System.out.println(listOfFiles[i].getName().replace(".properties", ""));
            servers.add(i, listOfFiles[i].getName().replace(".properties", ""));
        }

        for (int i = 0; i < servers.size(); i++) {
            String serverName = servers.get(i);
            MenuItem newServerName = new MenuItem(serverName);
            newServerName.setId(serverName);
            newServerName.setOnAction(event -> {
                Properties props = new Properties();
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                InputStream stream = loader.getResourceAsStream("com/scotch/OARKit/assets/servers/" + serverName + ".properties");
                try {
                    props.load(stream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(props);
                NameField.setText(props.getProperty("serverName"));
                IPField.setText(props.getProperty("serverIP"));
                PortField.setText(props.getProperty("serverPort"));

            });
            Platform.runLater(() -> configSelector.getItems().addAll(newServerName));
        }

        DeleteButton.setOnAction(event -> {
            //Path path = "com/scotch/OARKit/assets/servers/"+NameField.getText()+".properties";
            Path path = FileSystems.getDefault().getPath("com/scotch/OARKit/assets/servers/"+NameField.getText()+".properties");
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Connection Deleted");
            IPField.setText("");
            PortField.setText("");
            NameField.setText("");
            NetWindow = (Stage) DeleteButton.getScene().getWindow();
            NetWindow.close();
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
            } else if (IPField.getText().isEmpty()||!IPField.getText().matches("[0-9.]*")) {
                try {
                    Stage NetWindow = new Stage();
                    Parent NetRoot = FXMLLoader.load(getClass().getClassLoader().getResource("com/scotch/OARKit/java/NetworkManagerErrors/NetworkManagerErrorMessageIP.fxml"));
                    NetWindow.setTitle("IP Error");
                    NetWindow.setScene(new Scene(NetRoot, 200, 117));
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
                Properties props = new Properties();
                props.setProperty("serverName", NameField.getText());
                props.setProperty("serverIP", IPField.getText());
                props.setProperty("serverPort", PortField.getText());
                System.out.println(props);
                String path = "com/scotch/OARKit/assets/servers/" + NameField.getText() + ".properties";

                File f = new File(path);
                try {
                    f.createNewFile();
                    FileWriter writer = new FileWriter(f);
                    writer.write("serverName=" + NameField.getText());
                    writer.write("\n" + "serverIP=" + IPField.getText());
                    writer.write("\n" + "serverPort=" + PortField.getText());
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }/**/

                NetWindow = (Stage) SaveButton.getScene().getWindow();
                NetWindow.close();

                //String serverName=NameField.getText();
                //Controller.AddConfigToList(serverName);
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
