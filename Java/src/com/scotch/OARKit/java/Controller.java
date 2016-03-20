package com.scotch.OARKit.java;

import com.scotch.OARKit.java.Command.BaseCommand;
import com.scotch.OARKit.java.Command.Commands;
import com.scotch.OARKit.java.Command.Interpreter;
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

import java.lang.Thread;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.SocketException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable, Runnable{

    public static boolean running = true;

    public static ServerConnect serverConnect;
    public Label ethernet;
    public Label wifi4;
    public Label wifi2;
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
    @FXML
    ProgressBar StrengthBar;
    @FXML
    Label notConnectedLabel;
    @FXML
    Label StrengthLabel;
    @FXML
    Button addNewConfiguration;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    }

    @Override
    public void run() {
        while(running) try {
            networkManager.update();
            Platform.runLater(() -> StrengthBar.setProgress(networkManager.getSignalStrength()));
            Platform.runLater(() -> StrengthLabel.setText("Strength: " + networkManager.getRawSignalStrength() + " Dbm"));
            if (networkManager.getSignalStrength() == -1){
                Platform.runLater(() ->notConnectedLabel.setVisible(true));
                Platform.runLater(() ->StrengthBar.setVisible(false));
                Platform.runLater(() ->wifi2.setVisible(false));
                Platform.runLater(() ->StrengthLabel.setVisible(false));
                Platform.runLater(() ->wifi4.setVisible(false));
                Platform.runLater(() ->ethernet.setVisible(false));
                //REPEATS THE OUTPUT
                //System.out.println("Not connected to wifi.");
            } else {
                Platform.runLater(() ->notConnectedLabel.setVisible(false));
                Platform.runLater(() ->StrengthBar.setVisible(true));
                Platform.runLater(() ->wifi2.setVisible(true));
                Platform.runLater(() ->StrengthLabel.setVisible(true));
                Platform.runLater(() ->wifi4.setVisible(true));
                Platform.runLater(() ->ethernet.setVisible(false));
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
