package com.scotch.OARKit.java;

import com.scotch.OARKit.java.Command.BaseCommand;
import com.scotch.OARKit.java.Command.Commands;
import com.scotch.OARKit.java.Command.Interpreter;
import com.scotch.OARKit.java.helpers.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.*;
import java.lang.Thread;

import java.net.SocketException;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable, Runnable{

    public static boolean running = true;
    public static boolean connected = true;

    public static ServerConnect serverConnect;

    @FXML
    Button StopServer;
    @FXML
    WebView CameraWebView;
    WebEngine engine;
    @FXML
    TextArea consoleLog;
    @FXML
    TextField consoleTextField;
    @FXML
    Button restartNginx;
    @FXML
    Button sendButton;
    @FXML
    ToggleButton connectButton;
    public static ToggleButton connectButton1;
    @FXML
    ProgressBar StrengthBar;
    @FXML
    Label StrengthDBMLabel;
    @FXML
    Label StrengthLabel;
    @FXML
    Button addNewConfiguration;
    @FXML
    Label ConnectionType;
    @FXML
    ToggleButton on;
    @FXML
    ToggleButton off;
    @FXML
    MenuButton ipSelector;
    public static MenuButton ipSelector1;
    @FXML
    Label nameLabel;
    public static Label nameLabel1;
    @FXML
    Label ipLabel;
    public static Label ipLabel1;
    @FXML
    Label portLabel;
    public static Label portLabel1;

    @FXML
    ProgressBar LeftX;
    @FXML
    ProgressBar LeftY;
    @FXML
    ProgressBar RightX;
    @FXML
    ProgressBar RightY;

    Console console;
    PrintStream ps;
    //Keep old output system
    PrintStream out;
    //PrintStream err;
    NetworkManager networkManager;

    gamepad gamepad;

    String currentip;
    int currentport;

    public void print(String out) {
        System.out.println(out);
    }

    public static void AddConfigToList(String serverName) {
        MenuItem newServerName =new MenuItem(serverName);
        newServerName.setId(serverName);
        newServerName.setOnAction(event -> {
            Properties props = new Properties();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream stream = loader.getResourceAsStream("com/scotch/OARKit/assets/servers/"+serverName+".properties");
            try {
                props.load(stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(props);
            nameLabel1.setText("Name: "+props.getProperty("serverName"));
            ipLabel1.setText("IP: "+props.getProperty("serverIP"));
            portLabel1.setText("Port: "+props.getProperty("serverPort"));
            if (ServerConnect.connected){
                ServerDisconnect();
            }
        });
        Platform.runLater(() -> ipSelector1.getItems().addAll(newServerName));
    }

    public static void ServerDisconnect() {
        connectButton1.setSelected(false);
        System.out.println("Closing Socket");
        serverConnect.socketClose();
        connectButton1.setText("Connect");
        connected = false;
    }

    public void ServerConnect(String name, String ip, String port) {
        serverConnect = new ServerConnect(ip, port);
        engine.load("http://"+ip);
        System.out.println("Connected to new Server "+name+" ("+ip+", "+port+")");
        connectButton.setText("Disconnect");
        connected = true;
    }

    public void ToggleServerConnection(String name, String ip, String port) {
        if (!ServerConnect.connected){
            ServerConnect(name, ip, port);
        }else {
            ServerDisconnect();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            createEvents();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ipSelector1 = ipSelector;
        connectButton1 = connectButton;
        nameLabel1 = nameLabel;
        ipLabel1 = ipLabel;
        portLabel1 = portLabel;
        new GetServerList("com/scotch/OARKit/assets/properties/servers.sList");
        String stringFromFile = GetServerList.stringFromFile;
        //System.out.println(stringFromFile);
        String[] serverList = stringFromFile.split("\n");
        for (int i = 0; i < serverList.length; i++) {
            print("Server "+(i+1)+": "+serverList[i]);
            String[] server = serverList[i].split(", ");
            print((i+1)+" "+String.valueOf(server));
        }
        try {
            networkManager = new NetworkManager();
        } catch (IOException e) {
            e.printStackTrace();
        }
        gamepad = new gamepad();
        gamepad.gamepad();
        out = System.out;
        //err = System.err;
        console = new Console(consoleLog);
        ps = new PrintStream(console, true);
        redirectOutput(ps);
        engine = CameraWebView.getEngine();
        new Thread(this).start();
        if(Main.properties.getProperty("insideDev").equals("true")){
            //System.out.println("Inside Dev Environment");
            engine.load("http://www.google.com");
            //connectIP.setText("192.168.100.1");
            //engine.loadContent("");
            consoleLog.setText("Inside Dev Environment - Console Will Log but Commands will be ignored!!\n");
        }
        if(ServerConnect.connected&&Main.properties.getProperty("insideDev").equals("false")){
            connectButton.setSelected(true);
            engine.load("http://192.168.100.1");
        }
    }

    public void createEvents() throws IOException {
        restartNginx.setOnAction(event -> new Interpreter("print hello").returnCommand().runCommand());
        StopServer.setOnAction(event -> new Interpreter("stopserver").returnCommand().runCommand());

        sendButton.setOnAction(event -> {
            new Interpreter(consoleTextField.getText()).returnCommand().runCommand();
            consoleTextField.setText("");
        });

        addNewConfiguration.setOnAction(event -> {
            try {
                Stage NetWindow = new Stage();
                Parent NetRoot = FXMLLoader.load(getClass().getClassLoader().getResource("com/scotch/OARKit/assets/layout/NetworkManager.fxml"));
                NetWindow.setTitle("Network Manager");
                NetWindow.setScene(new Scene(NetRoot, 358, 191));
                NetWindow.setAlwaysOnTop(true);
                NetWindow.setResizable(false);
                NetWindow.show();
            } catch (IOException e){
                e.printStackTrace();
            }
        });

        File folder = new File("com/scotch/OARKit/assets/servers");
        File[] listOfFiles = folder.listFiles();
        List<String> servers = new ArrayList<>();

        for (int i = 0; i < listOfFiles.length; i++) {
            //System.out.println(listOfFiles[i].getName().replace(".properties", ""));
            servers.add(i, listOfFiles[i].getName().replace(".properties", ""));
        }

        for (int i = 0; i < servers.size(); i++) {
            String serverName=servers.get(i);
            AddConfigToList(serverName);
        }/**/

        connectButton.setOnAction(event -> {
            if (!nameLabel.getText().equals("Name:")) {
                Properties props = new Properties();
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                InputStream stream = loader.getResourceAsStream("com/scotch/OARKit/assets/servers/"+nameLabel.getText().replace("Name: ", "")+".properties");
                try {
                    props.load(stream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ToggleServerConnection(props.getProperty("serverName"), props.getProperty("serverIP"), props.getProperty("serverPort"));
            } else {
                print("Please select a server");
            }
        });

        /*on.setOnAction(event -> {
            on.setDisable(true);
            off.setDisable(false);
        });

        off.setOnAction(event -> {
            off.setDisable(true);
            on.setDisable(false);
        });/**/
    }

    @Override
    public void run() {
        while(running) try {
            try{networkManager.update();} catch (SocketException e) {}
            Platform.runLater(() -> StrengthBar.setProgress(networkManager.getSignalStrength()));
            Platform.runLater(() -> StrengthDBMLabel.setText("Strength: "+networkManager.getRawSignalStrength()+ " Dbm"));
            Platform.runLater(() -> ConnectionType.setText("Connection: "+networkManager.getConnectionType()));

            if (networkManager.getSignalStrength() == -1){
                Platform.runLater(() ->StrengthDBMLabel.setVisible(false));
                Platform.runLater(() ->StrengthBar.setVisible(false));
                Platform.runLater(() ->StrengthLabel.setVisible(false));
                //REPEATS THE OUTPUT
                //System.out.println("Not connected to wifi.");
            } else {
                Platform.runLater(() ->StrengthDBMLabel.setVisible(true));
                Platform.runLater(() ->StrengthBar.setVisible(true));
                Platform.runLater(() ->StrengthLabel.setVisible(true));
            }
            if (gamepad.connected){
                gamepad.pollgamepad();
                Platform.runLater(() -> LeftX.setProgress(gamepad.leftstickx/100));
                Platform.runLater(() -> LeftY.setProgress(gamepad.leftsticky/100));

                Platform.runLater(() -> RightX.setProgress(gamepad.rightstickx/100));
                Platform.runLater(() -> RightY.setProgress(gamepad.rightsticky/100));
            }
            if (!connected){
                Platform.runLater(() -> connectButton.setText("Connect"));
                Platform.runLater(() -> connectButton.setSelected(false));
            }
            if (!consoleTextField.getText().isEmpty()) {
                sendButton.setDisable(false);
            } else {
                sendButton.setDisable(true);
            }
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Defines Output Stream
    private class Console extends OutputStream {

        private TextArea txtArea;

        public Console(TextArea txtArea) {
            this.txtArea = txtArea;
        }

        @Override
        public void write(int b) throws IOException {
            Platform.runLater(() -> txtArea.appendText(String.valueOf((char) b)));
            out.print(String.valueOf((char) b));
        }

    }
    public void redirectOutput(PrintStream printStream){
        System.setOut(printStream);
        System.setErr(printStream);
    }
    public static void disconnectServer(){
        connected = false;
    }/**/
}
