package com.scotch.OARKit.java.helpers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class NetworkManagerErrorMesage implements Initializable {

    @FXML
    Stage NetWindow;
    @FXML
    Button closeButton;
    @FXML
    Label errorName;
    @FXML
    Label errorText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createEvents();
        String errorType=NetworkManager.errorType1;
        errorName.setText(errorType+" Error");
        errorText.setText("Please input a valid "+errorType.toLowerCase());
    }

    public void createEvents(){
        closeButton.setOnAction(event -> {
            NetWindow = (Stage) closeButton.getScene().getWindow();
            NetWindow.close();
        });
    }
}
