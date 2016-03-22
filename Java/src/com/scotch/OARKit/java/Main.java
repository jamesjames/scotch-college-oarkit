package com.scotch.OARKit.java;

import com.scotch.OARKit.java.helpers.JInputJoystick;
import com.scotch.OARKit.java.helpers.ServerConnect;
import com.scotch.OARKit.java.helpers.gamepad;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



import java.io.IOException;
import java.util.Properties;


import static com.scotch.OARKit.java.helpers.NativeLoader.*;

public class Main extends Application {
    public static Properties properties;

    @Override
    public void stop(){
        System.out.println("Quitting Application");
        Controller.serverConnect.socketClose();
        Controller.running = false;
        // Save file
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("com/scotch/OARKit/assets/layout/MainScreen.fxml"));
        primaryStage.setTitle("Scotch OAR Kit");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();

    }


    public static void main(String[] args) throws IOException {
        System.out.println(System.getProperty("os.name"));
        properties = new Properties();
        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("com/scotch/OARKit/assets/properties/default.properties"));
        if (args.length > 0 && args[0].equals("server")){
            new Server();
        }else{
            startLibraries();
            launch(args);
        }
    }
    public static void startLibraries() throws IOException {
        String os = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch");
        if (os.contains("win")){
            loadLib("/jinput-dx8.dll");
            loadLib("/jinput-dx8_64.dll");
            loadLib("/jinput-raw.dll");
            loadLib("/jinput-raw_64.dll");
            loadLib("/jinput-wintab.dll");
        } else if(os.contains("mac")){
            loadLib("/libjinput-osx.jnilib");
        } else if(os.contains("nux")){
            //If this gives an Exception, update the maven project libraries with package.
            loadLib("/libjinput-linux.so");
            loadLib("/libjinput-linux64.so");
        }else{
            System.out.println("Your OS is Not Supported! Please report FULL log to ScotchOARKit on GitHub");
            System.exit(0);
        }

    }
}
