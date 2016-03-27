package com.scotch.OARKit.java.helpers;



// TODO add a connection manager backend.

import com.scotch.OARKit.java.Controller;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.python.antlr.ast.Delete;

import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createEvents();
    }

    public void createEvents(){
        DeleteButton.setOnAction(event -> {
        });/**/

        CancelButton.setOnAction(event -> {
            NetWindow = (Stage) CancelButton.getScene().getWindow();
            NetWindow.close();
        });

        SaveButton.setOnAction(event -> {
            Properties props = new Properties();
            props.setProperty("serverName",NameField.getText());
            props.setProperty("serverIP",IPField.getText());
            props.setProperty("serverPort",PortField.getText());
            System.out.println(props);
            String path = "com/scotch/OARKit/assets/servers/"+NameField.getText()+".properties";

            File f = new File(path);
            try {
                f.createNewFile();
                FileWriter writer = new FileWriter(f);
                writer.write("serverName='"+NameField.getText()+"'");
                writer.write("\n"+"serverIP="+IPField.getText());
                writer.write("\n"+"serverPort="+PortField.getText());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }/**/

            NetWindow = (Stage) SaveButton.getScene().getWindow();
            NetWindow.close();

            /*String serverName=NameField.getText();
            MenuItem newServerName =new MenuItem(serverName);
            newServerName.setId(serverName);
            newServerName.setOnAction(event1 -> {
                Properties prop = new Properties();
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                InputStream stream = loader.getResourceAsStream("com/scotch/OARKit/assets/servers/"+serverName+".properties");

                try {
                    prop.load(stream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(prop);
                Platform.runLater(() -> Controller.connectIP1.setText(prop.getProperty("serverIP")));
                if (ServerConnect.connected) {
                    Platform.runLater(() -> Controller.connectButton1.setSelected(false));
                    System.out.println("Closing Socket");
                    Controller.serverConnect.socketClose();
                    Platform.runLater(() -> Controller.connectButton1.setText("Connect"));
                }
            });
            Platform.runLater(() -> Controller.ipSelector1.getItems().addAll(newServerName));/**/
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
