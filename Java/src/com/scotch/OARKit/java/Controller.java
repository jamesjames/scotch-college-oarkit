package com.scotch.OARKit.java;

import com.scotch.OARKit.java.Command.BaseCommand;
import com.scotch.OARKit.java.Command.Commands;
import com.scotch.OARKit.java.Command.Interpreter;
import com.scotch.OARKit.java.helpers.NetworkManager;
import com.scotch.OARKit.java.helpers.ServerConnect;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.lang.Thread;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable, Runnable{

    public static boolean running = true;

    public static ServerConnect serverConnect;
    //FOR CAMERA
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
    @FXML
    TextField connectIP;

    Console console;
    PrintStream ps;
    //Keep old output system
    PrintStream out;
    //PrintStream err;
    NetworkManager networkManager;
    @FXML
    ProgressBar StrengthBar;
    @FXML
    Label notConnectedLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        networkManager = new NetworkManager();
        out = System.out;
        //err = System.err;
        console = new Console(consoleLog);
        ps = new PrintStream(console, true);
        redirectOutput(ps);
        serverConnect = new ServerConnect();
        engine = CameraWebView.getEngine();
        new Thread(this).start();
        if(Main.properties.getProperty("insideDev").equals("true")){
            System.out.println("Inside Dev Enviroment");
            engine.load("http://www.google.com");
            //engine.loadContent("");
            consoleLog.setText("Inside Dev Environment - Console Will Log but Commands will be ignored!!\n");
        }
        createEvents();
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
            if (ServerConnect.connected){
                connectButton.setSelected(false);
                System.out.println("Closing Socket");
                serverConnect.socketClose();
            }else {
                serverConnect = new ServerConnect(connectIP.getText());
                engine.load("http://"+connectIP.getText());
                System.out.println("Connected to new Server " + connectIP.getText());
            }
        });
    }

    @Override
    public void run() {
        while(running) try {
            networkManager.update();
            StrengthBar.setProgress(networkManager.getSignalStrength());
            if (networkManager.getSignalStrength() == -1){
                notConnectedLabel.setVisible(true);
                System.out.println("Not connected to wifi.");
            } else {
                notConnectedLabel.setVisible(false);
            }
            Thread.sleep(1000);
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
            txtArea.appendText(String.valueOf((char) b));
            out.print(String.valueOf((char) b));
        }

    }
    public void redirectOutput(PrintStream printStream){
        System.setOut(printStream);
        System.setErr(printStream);
    }
}
