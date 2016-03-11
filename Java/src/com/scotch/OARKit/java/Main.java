package com.scotch.OARKit.java;

import com.scotch.OARKit.java.helpers.ServerConnect;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class Main extends Application {
    public static Properties properties;

    @Override
    public void stop(){
        System.out.println("Quitting Application");
        Controller.serverConnect.socketClose();
        // Save file
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("com/scotch/OARKit/assets/layout/Screen.fxml"));
        primaryStage.setTitle("Scotch OAR Kit");
        primaryStage.setScene(new Scene(root, 480, 700));
        primaryStage.show();

    }


    public static void main(String[] args) throws IOException {
        properties = new Properties();
        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("com/scotch/OARKit/assets/properties/default.properties"));
        if (args.length > 0 && args[0].equals("server")){
            new Server();
        }else{

            launch(args);

        }

    }
}
