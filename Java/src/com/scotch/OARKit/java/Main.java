package com.scotch.OARKit.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.python.util.PythonInterpreter;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("com/scotch/OARKit/assets/layout/Screen.fxml"));
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setTitle("Scotch OAR Kit");
        primaryStage.setScene(new Scene(root, primaryScreenBounds.getWidth()/2, primaryScreenBounds.getHeight()));
        primaryStage.show();
    }


    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("server")){
            PythonInterpreter interp = new PythonInterpreter();
            interp.exec("import sys");
            //ALLOWS US TO PASS ARGS
            //interp.exec("sys.argv = ['debug']");
            interp.execfile("com/scotch/OARKit/python/Server-2.py");
        }else{
            launch(args);
        }

    }
}
