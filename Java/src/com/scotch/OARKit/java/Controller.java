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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.lang.Thread;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable, EventHandler {

    public static boolean running = true;

    public static ServerConnect serverConnect;
    //FOR CAMERA
    @FXML
    WebView CameraWebView;
    WebEngine engine;
    @FXML
    Label consoleLog;
    @FXML
    TextField consoleTextField;
    @FXML
    Button restartNginx;
    @FXML
    Button sendButton;

    @FXML
    ProgressBar StrengthBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(() -> update()).start();
        if(Main.properties.getProperty("insideDev").equals("true")){
            System.out.println("Inside Dev Enviroment");
            engine = CameraWebView.getEngine();
            engine.load("http://www.google.com");
            //engine.loadContent("");
            consoleLog.setText("Inside Dev Environment - Console Will Log but Commands will be ignored!!");
        }

        restartNginx.setOnAction(event -> new Interpreter("print hello").returnCommand().runCommand());
        sendButton.setOnAction(event -> {
            new Interpreter(consoleTextField.getText()).returnCommand().runCommand();
            consoleTextField.setText("");
        });
    }

    @Override
    public void handle(Event event) {

    }

    public void update() {
        while(running) try {
            float signalStrength = NetworkManager.getStrength();
            StrengthBar.setProgress(signalStrength);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
