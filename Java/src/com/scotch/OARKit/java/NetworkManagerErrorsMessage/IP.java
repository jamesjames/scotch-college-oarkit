package com.scotch.OARKit.java.NetworkManagerErrorsMessage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class IP implements Initializable {

    @FXML
    Stage NetWindow;
    @FXML
    Button closeButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createEvents();
    }

    public void createEvents(){
        closeButton.setOnAction(event -> {
            NetWindow = (Stage) closeButton.getScene().getWindow();
            NetWindow.close();
        });
    }
}
