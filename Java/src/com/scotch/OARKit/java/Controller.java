package com.scotch.OARKit.java;

import com.scotch.OARKit.java.Command.BaseCommand;
import com.scotch.OARKit.java.Command.Commands;
import com.scotch.OARKit.java.Command.Interpreter;
import com.scotch.OARKit.java.helpers.GetServerList;
import com.scotch.OARKit.java.helpers.NetworkManager;
import com.scotch.OARKit.java.helpers.ServerConnect;
import com.scotch.OARKit.java.helpers.gamepad;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

public class Controller implements Initializable, Runnable{

    public static boolean running = true;

    public static ServerConnect serverConnect;

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
    TextField connectIP;
    public static TextField connectIP1;
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
    public  static MenuButton ipSelector1;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createEvents();
        ipSelector1 = ipSelector;
        connectIP1 = connectIP;
        connectButton1 = connectButton;
        new GetServerList("com/scotch/OARKit/assets/properties/servers.sList");
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
            System.out.println("Inside Dev Enviroment");
            engine.load("http://www.google.com");
            connectIP.setText("192.168.100.1");
            //engine.loadContent("");
            consoleLog.setText("Inside Dev Environment - Console Will Log but Commands will be ignored!!\n");
        }
        if(ServerConnect.connected&&Main.properties.getProperty("insideDev").equals("false")){
            connectButton.setSelected(true);
            engine.load("http://192.168.100.1");
        }
    }

    public void createEvents(){
        restartNginx.setOnAction(event -> new Interpreter("print hello").returnCommand().runCommand());

        sendButton.setOnAction(event -> {
            new Interpreter(consoleTextField.getText()).returnCommand().runCommand();
            consoleTextField.setText("");
        });

        connectButton.setOnAction(event -> {
            if (!ServerConnect.connected){
                serverConnect = new ServerConnect(connectIP.getText(), 5006);
                engine.load("http://"+connectIP.getText());
                System.out.println("Connected to new Server " + connectIP.getText());
                connectButton.setText("Disconnect");
            }else {
                connectButton.setSelected(false);
                System.out.println("Closing Socket");
                serverConnect.socketClose();
                connectButton.setText("Connect");
            }
        });

        addNewConfiguration.setOnAction(event -> {
            try {
                Stage NetWindow = new Stage();
                Parent NetRoot = FXMLLoader.load(getClass().getClassLoader().getResource("com/scotch/OARKit/assets/layout/NetworkManager.fxml"));
                NetWindow.setTitle("NetworkManager");
                NetWindow.setScene(new Scene(NetRoot, 420, 120));
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
            MenuItem newServerName =new MenuItem(serverName);
            newServerName.setId(serverName);
            newServerName.setOnAction(event -> {
                connectIP.setText(newServerName.getText());
                if (ServerConnect.connected){
                    connectButton.setSelected(false);
                    System.out.println("Closing Socket");
                    serverConnect.socketClose();
                    connectButton.setText("Connect");
                }
            });
            Platform.runLater(() -> ipSelector.getItems().addAll(newServerName));
        }

        /*on.setOnAction(event -> {
            on.setDisable(true);
            off.setDisable(false);
        });

        off.setOnAction(event -> {
            off.setDisable(true);
            on.setDisable(false);
        });/**/

        /*CancelButton.setOnAction(event -> {
            NetWindow = (Stage) CancelButton.getScene().getWindow();
            NetWindow.close();
        });

        SaveButton.setOnAction(event -> {
            Properties props = new Properties();
            props.setProperty("serverName",NameField.getText());
            props.setProperty("serverIP",IPField.getText());
            props.setProperty("serverPort",PortField.getText());
            System.out.println(props);
            File f = new File(NameField.getText()+".properties");
            /*try {
                OutputStream out = new FileOutputStream(f);
                out.write(props.);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }/**
            NetWindow = (Stage) SaveButton.getScene().getWindow();
            NetWindow.close();
        });/**/

    }

    @Override
    public void run() {
        while(running) try {
            try{networkManager.update();} catch (SocketException e) {}
            Platform.runLater(() -> StrengthBar.setProgress(networkManager.getSignalStrength()));
            Platform.runLater(() -> StrengthDBMLabel.setText("Strength: " + networkManager.getRawSignalStrength() + " Dbm"));
            Platform.runLater(() -> ConnectionType.setText("Connection: " + networkManager.getConnectionType()));
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
}
