package com.scotch.OARKit.java;

import com.scotch.OARKit.java.helpers.ServerConnect;
import javafx.event.ActionEvent;
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
    WebView webView;
    WebEngine engine;
    @FXML
    Label consoleLog;
    @FXML
    TextField consoleTextField;
    @FXML
    Button restartNginx;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //System.out.println(p.getProperty("insideDev"));
        if(Main.properties.getProperty("insideDev").equals("true")){
            engine = webView.getEngine();
            engine.loadContent("Inside Dev Environment - No Camera :(");
            consoleLog.setText("Inside Dev Environment - Console Will Log but Commands will be ignored!!");
        }

        restartNginx.setOnAction(event -> serverConnect.sendData("EHello"));
    }

    @Override
    public void handle(Event event) {

    }


}
