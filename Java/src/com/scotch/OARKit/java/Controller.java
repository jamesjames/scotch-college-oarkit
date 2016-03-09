package com.scotch.OARKit.java;

import com.scotch.OARKit.java.Command.BaseCommand;
import com.scotch.OARKit.java.Command.Commands;
import com.scotch.OARKit.java.helpers.ServerConnect;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable, EventHandler{
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(Main.properties.getProperty("insideDev").equals("true")){
            System.out.println("Inside Dev Enviroment");
            engine = CameraWebView.getEngine();
            engine.load("http://www.google.com");
            //engine.loadContent("");
            consoleLog.setText("Inside Dev Environment - Console Will Log but Commands will be ignored!!");
        }

        restartNginx.setOnAction(event -> new Commands(BaseCommand.ECHO,"Hello").runCommand());
    }

    @Override
    public void handle(Event event) {

    }


}
