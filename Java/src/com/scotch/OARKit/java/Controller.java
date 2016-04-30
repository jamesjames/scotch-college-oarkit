package com.scotch.OARKit.java;

import com.scotch.OARKit.java.Command.Interpreter;
import com.scotch.OARKit.java.Command.Interpreter_test;
import com.scotch.OARKit.java.ServerList.*;
import com.scotch.OARKit.java.helpers.*;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.*;
import javafx.util.Duration;
import net.java.games.input.Component;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.lang.Thread;

import java.net.SocketException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

public class Controller implements Initializable, Runnable{

    public static boolean running = true;
    public static boolean connected = false;
    public static boolean manualControl = false;

    public static ServerConnect serverConnect;

    @FXML
    Stage MainStage;
    @FXML
    Scene MainScene;
    @FXML
    javafx.stage.Window MainWindow;

    @FXML
    WebView CameraWebView;
    WebEngine engine;
    @FXML
    TextArea consoleLog;
    @FXML
    TextField consoleTextField;
    @FXML
    Button sendButton;
    @FXML
    TitledPane consoleTitle;

    @FXML
    ProgressBar LeftBackwardIndicator;
    @FXML
    ProgressBar LeftForwardIndicator;
    @FXML
    Slider LeftPowerSlider;
    @FXML
    ProgressBar RightBackwardIndicator;
    @FXML
    ProgressBar RightForwardIndicator;
    @FXML
    Slider RightPowerSlider;

    @FXML
    ToggleButton ButtonY;
    @FXML
    ToggleButton ButtonB;
    @FXML
    ToggleButton ButtonA;
    @FXML
    ToggleButton ButtonX;
    @FXML
    ToggleButton ButtonUp;
    @FXML
    ToggleButton ButtonDown;
    @FXML
    ToggleButton ButtonRight;
    @FXML
    ToggleButton ButtonLeft;
    @FXML
    ToggleButton ButtonLT;
    @FXML
    ToggleButton ButtonLB;
    @FXML
    ToggleButton ButtonRT;
    @FXML
    ToggleButton ButtonRB;
    @FXML
    ProgressBar LeftX;
    @FXML
    ProgressBar LeftY;
    @FXML
    ProgressBar RightX;
    @FXML
    ProgressBar RightY;
    @FXML
    Label ControllerStatus;
    @FXML
    Button AddController;
    @FXML
    ProgressBar LeftTriggerBar;
    @FXML
    ProgressBar RightTriggerBar;
    @FXML
    Label TriggersLadle;
    @FXML
    ProgressBar LeftXNegative;
    @FXML
    ProgressBar LeftYNegative;
    @FXML
    ProgressBar LeftXPositive;
    @FXML
    ProgressBar LeftYPositive;
    @FXML
    ProgressBar RightXNegative;
    @FXML
    ProgressBar RightYNegative;
    @FXML
    ProgressBar RightXPositive;
    @FXML
    ProgressBar RightYPositive;
    @FXML
    ToggleButton ButtonRightStick;
    @FXML
    ToggleButton ButtonLeftStick;

    @FXML
    Label ServerConnectionStatus;
    @FXML
    Button StopServer;
    @FXML
    Button restartNginx;
    @FXML
    ToggleButton connectButton;
    public static ToggleButton connectButton1;
    @FXML
    ProgressBar StrengthBar;
    @FXML
    Label StrengthDBMLabel;
    @FXML
    Label StrengthLabel;
    @FXML
    Button addNewConfiguration;
    @FXML
    Label ConnectionType;
    @FXML
    MenuButton ipSelector;
    public static MenuButton ipSelector1;
    @FXML
    Label nameLabel;
    public static Label nameLabel1;
    @FXML
    Label ipLabel;
    public static Label ipLabel1;
    @FXML
    Label portLabel;
    public static Label portLabel1;

    @FXML
    ToggleButton DevModeOn;
    @FXML
    ToggleButton DevModeOff;

    @FXML
    ToggleButton ManualControlOn;
    @FXML
    ToggleButton ManualControlOff;
    @FXML
    Button FitWindowToScreenButton;
    @FXML
    Button ResetWindowSizeButton;
    @FXML
    Button DetectControllerButton;

    double leftX;
    double leftY;
    double rightX;
    double rightY;

    Console console;
    PrintStream ps;
    //Keep old output system
    PrintStream out;
    //PrintStream err;
    NetworkManager networkManager;

    gamepad gamepad;

    String currentip;
    int currentport;

    public void currentTime1() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Timeline currentTime;
        currentTime = new Timeline(new KeyFrame(Duration.seconds(1), event -> consoleTitle.setText("Console - Time: "+format.format(Calendar.getInstance().getTime()))));
        currentTime.setCycleCount(Animation.INDEFINITE);
        currentTime.play();
    }

    //this is meant to be called with in the running loop for each button on the controller and should provide feedback in the UI of what buttons are pressed. i don't know how to test if a button is pressed so i have used console input to test
    public void controllerButtons(Boolean ButtonPressed, ToggleButton FeedBackViewer) {
        if (ButtonPressed) {
            FeedBackViewer.setSelected(true);
        } else {
            FeedBackViewer.setSelected(false);
        }
    }

    //adds configs to the dropdown list
    public static void AddConfigToList() {
        ipSelector1.getItems().clear();
        String[] servers = ServerList.getKeys();

        for (int i = 0; i < servers.length; i++) {
            //Logger.info(servers[i]);
            MenuItem newServerName =new MenuItem(servers[i]);
            newServerName.setId(servers[i]);
            final int finalI = i;
            newServerName.setOnAction(event -> {
                String ip = ServerList.getIPAndPort(servers[finalI])[0];
                String port = ServerList.getIPAndPort(servers[finalI])[1];
                Logger.info("Loaded config '"+servers[finalI]+"' ("+ip+", "+port+")");
                nameLabel1.setText("Name: "+servers[finalI]);
                ipLabel1.setText("IP: "+ip);
                portLabel1.setText("Port: "+port);
                if (ServerConnect.connected){
                    ServerDisconnect();
                }
            });
            Platform.runLater(() -> ipSelector1.getItems().addAll(newServerName));
        }
    }

    public void restartWindow(Button clicked, int Width, int Height) {
        MainStage = (Stage) clicked.getScene().getWindow();
        MainStage.close();
        try {
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("com/scotch/OARKit/assets/layout/MainScreen.fxml"));
            primaryStage.setTitle("Scotch OAR Kit");
            primaryStage.setScene(new Scene(root, Width, Height));
            primaryStage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //disconects the server
    public static void ServerDisconnect() {
        if (ServerConnect.connected) {
            //connectButton1.setSelected(false);
            Logger.info("Closing Socket");
            serverConnect.socketClose();
            //connectButton1.setText("Connect");
            connected = false;
        }
    }

    //connects the server
    public void ServerConnect(String name, String ip, String port) {
        if (!ServerConnect.connected){
            serverConnect = new ServerConnect(ip, port);
            if (ServerConnect.connected == true) {
                engine.load("http://"+ip);
                Logger.info("Connected to new Server "+name+" ("+ip+", "+port+")");
                //connectButton.setText("Disconnect");
                connected = true;
            }
        }
    }

    //toggles the connection to the server
    public void ToggleServerConnection(String name, String ip, String port) {
        if (!ServerConnect.connected){
            ServerConnect(name, ip, port);
        }else {
            ServerDisconnect();
        }
    }

    private void NewGamePad() {
        gamepad = new gamepad();
        gamepad.gamepad();
        if (gamepad.gamepad.isControllerConnected()) {
            //Logger.info(type);
            if (gamepad.gamepad.getControllerType() == net.java.games.input.Controller.Type.GAMEPAD) {
                LeftTriggerBar.setVisible(true);
                RightTriggerBar.setVisible(true);
                TriggersLadle.setVisible(true);
                ButtonLT.setVisible(false);
                ButtonRT.setVisible(false);
            } else if (gamepad.gamepad.getControllerType() == net.java.games.input.Controller.Type.STICK) {
                LeftTriggerBar.setVisible(false);
                RightTriggerBar.setVisible(false);
                TriggersLadle.setVisible(false);
                ButtonLT.setVisible(true);
                ButtonRT.setVisible(true);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(!new File(OSUtils.osAppData()).exists()) new File(OSUtils.osAppData()).mkdirs();
        try {
            createEvents();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out = System.out;
        //err = System.err;
        console = new Console(consoleLog);
        ps = new PrintStream(console, true);
        redirectOutput(ps);
        ipSelector1 = ipSelector;
        connectButton1 = connectButton;
        nameLabel1 = nameLabel;
        ipLabel1 = ipLabel;
        portLabel1 = portLabel;
        currentTime1();
        new GetServerList("com/scotch/OARKit/assets/properties/servers.sList");
        try {
            networkManager = new NetworkManager();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AddConfigToList();

        engine = CameraWebView.getEngine();
        if(Main.DevMode.equals("true")){
            DevModeOn.setDisable(true);
            DevModeOn.setSelected(true);
            DevModeOff.setDisable(false);
            DevModeOff.setSelected(false);
            //Logger.info("Inside Dev Environment");
            engine.load("http://c.xkcd.com/random/comic/");
            //connectIP.setText("192.168.100.1");
            //engine.loadContent("");
            Logger.info("Inside Dev Environment");
        } else {
            DevModeOff.setDisable(true);
            DevModeOff.setSelected(true);
            DevModeOn.setDisable(false);
            DevModeOn.setSelected(false);
            Logger.info("Type \"help\" for a list of commands");
        }
        if(ServerConnect.connected&&Main.DevMode.equals("false")){
            //connectButton.setSelected(true);
            connected = true;
            engine.load("http://192.168.100.1");
        }
        NewGamePad();
        new Thread(this).start();
    }

    public void createEvents() throws IOException {
        //restartNginx.setOnAction(event -> new Interpreter("print hello").returnCommand().runCommand());
        restartNginx.setOnAction(event -> Interpreter_test.interpreter("print hello"));
        //StopServer.setOnAction(event -> new Interpreter("stopserver").returnCommand().runCommand());
        StopServer.setOnAction(event -> Interpreter_test.interpreter("stopserver"));

        DevModeOn.setOnAction(event -> {
            Main.DevMode="true";
            restartWindow(DetectControllerButton, 1024, 768);
        });

        DevModeOff.setOnAction(event -> {
            Main.DevMode="false";
            restartWindow(DetectControllerButton, 1024, 768);
        });

        DetectControllerButton.setOnAction(event -> {
            restartWindow(DetectControllerButton, 1024, 768);
        });

        FitWindowToScreenButton.setOnAction(event -> {
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            restartWindow(FitWindowToScreenButton, (int) screenBounds.getWidth(), (int) screenBounds.getHeight());
        });

        ResetWindowSizeButton.setOnAction(event -> {
            restartWindow(ResetWindowSizeButton, 1024, 768);
        });

        sendButton.setOnAction(event -> {
            //new Interpreter(consoleTextField.getText().toLowerCase()).returnCommand().runCommand();
            Interpreter_test.interpreter(consoleTextField.getText());
            consoleTextField.setText("");
        });

        addNewConfiguration.setOnAction(event -> {
            try {
                Stage NetWindow = new Stage();
                Parent NetRoot = FXMLLoader.load(getClass().getClassLoader().getResource("com/scotch/OARKit/assets/layout/NetworkManager.fxml"));
                NetWindow.setTitle("Network Manager");
                NetWindow.setScene(new Scene(NetRoot, 358, 191));
                NetWindow.setAlwaysOnTop(true);
                NetWindow.setResizable(false);
                NetWindow.initModality(Modality.WINDOW_MODAL);
                NetWindow.initOwner(addNewConfiguration.getScene().getWindow());
                NetWindow.show();
            } catch (IOException e){
                e.printStackTrace();
            }
        });

        connectButton.setOnAction(event -> {
            if (!nameLabel.getText().equals("Name:")) {
                ToggleServerConnection(nameLabel.getText().replace("Name: ", ""), ipLabel.getText().replace("IP: ", ""), portLabel.getText().replace("Port: ", ""));
            } else {
                Logger.info("Please select a server");
                //connectButton.setSelected(false);
            }
        });

        AddController.setOnAction(event -> {
            /*boolean set=false;
            for (int d = 0; d < 1;) {
                if (!set) {
                    for (int i = 0; i < gamepad.gamepad.getNumberOfButtons(); i++) {
                        if (gamepad.gamepad.getButtonValue(i)) {
                            Logger.info("Button A assigned to: " + gamepad.gamepad.components[i]);
                            set=true;
                        }
                        //Logger.info(gamepad.gamepad.components[i]);
                    }
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                    }
                } else {
                    d=1;
                }
            }/**/
            if (gamepad.gamepad.isControllerConnected()) {
                GamePadMapper GamePadMapper = new GamePadMapper();
                GamePadMapper.start();
            } else {
                Logger.info("No controller connected");
            }
            /*ArrayList<Object> map = GamePadMapper.Map;
            Logger.info(map);
            String map1="";
            for (int i=0; i < map.size(); i++) {
                map1 = map1+", ";
                map1 = map1+map.get(i);
            }
            map1 = map1.replaceFirst(", ", "");
            Logger.info(map1);/**/
        });/**/

        ManualControlOn.setOnAction(event -> {
            ManualControlOn.setDisable(true);
            ManualControlOff.setDisable(false);
            ManualControlOff.setSelected(false);
        });
        ManualControlOff.setOnAction(event -> {
            ManualControlOff.setDisable(true);
            ManualControlOn.setDisable(false);
            ManualControlOn.setSelected(false);
        });
    }

    @Override
    public void run() {
        while(running) try {
            try{networkManager.update();} catch (SocketException e) {}
            Platform.runLater(() -> StrengthBar.setProgress(networkManager.getSignalStrength()));
            Platform.runLater(() -> StrengthDBMLabel.setText("Strength: "+networkManager.getRawSignalStrength()+ " Dbm"));
            Platform.runLater(() -> ConnectionType.setText("Network Connection: "+networkManager.getConnectionType()));

            if (networkManager.getSignalStrength() == -1){
                Platform.runLater(() ->StrengthDBMLabel.setVisible(false));
                Platform.runLater(() ->StrengthBar.setVisible(false));
                Platform.runLater(() ->StrengthLabel.setVisible(false));
                //REPEATS THE OUTPUT
                //Logger.info("Not connected to wifi.");
            } else {
                Platform.runLater(() ->StrengthDBMLabel.setVisible(true));
                Platform.runLater(() ->StrengthBar.setVisible(true));
                Platform.runLater(() ->StrengthLabel.setVisible(true));
            }
            if (gamepad.connected) {
                if (manualControl) {
                    gamepad.pollgamepad();

                    leftX = (gamepad.leftstickx / 100) - 0.5;
                    if (leftX < 0) {
                        Platform.runLater(() -> LeftXNegative.setProgress(-leftX * 2));
                        Platform.runLater(() -> LeftXPositive.setProgress(0));
                    } else if (leftX > 0) {
                        Platform.runLater(() -> LeftXNegative.setProgress(0));
                        Platform.runLater(() -> LeftXPositive.setProgress(leftX * 2));
                    } else {
                        Platform.runLater(() -> LeftXNegative.setProgress(0));
                        Platform.runLater(() -> LeftXPositive.setProgress(0));
                    }

                    leftY = (gamepad.leftsticky / 100) - 0.5;
                    if (leftY < 0) {
                        Platform.runLater(() -> LeftYNegative.setProgress(0));
                        Platform.runLater(() -> LeftYPositive.setProgress(-leftY * 2));
                    } else if (leftY > 0) {
                        Platform.runLater(() -> LeftYNegative.setProgress(leftY * 2));
                        Platform.runLater(() -> LeftYPositive.setProgress(0));
                    } else {
                        Platform.runLater(() -> LeftYNegative.setProgress(0));
                        Platform.runLater(() -> LeftYPositive.setProgress(0));
                    }

                    rightX = (gamepad.rightstickx / 100) - 0.5;
                    if (rightX < 0) {
                        Platform.runLater(() -> RightXNegative.setProgress(-rightX * 2));
                        Platform.runLater(() -> RightXPositive.setProgress(0));
                    } else if (rightX > 0) {
                        Platform.runLater(() -> RightXNegative.setProgress(0));
                        Platform.runLater(() -> RightXPositive.setProgress(rightX * 2));
                    } else {
                        Platform.runLater(() -> RightXNegative.setProgress(0));
                        Platform.runLater(() -> RightXPositive.setProgress(0));
                    }

                    rightY = (gamepad.rightsticky / 100) - 0.5;
                    if (rightY < 0) {
                        Platform.runLater(() -> RightYNegative.setProgress(0));
                        Platform.runLater(() -> RightYPositive.setProgress(-rightY * 2));
                    } else if (rightY > 0) {
                        Platform.runLater(() -> RightYNegative.setProgress(rightY * 2));
                        Platform.runLater(() -> RightYPositive.setProgress(0));
                    } else {
                        Platform.runLater(() -> RightYNegative.setProgress(0));
                        Platform.runLater(() -> RightYPositive.setProgress(0));
                    }

                    Platform.runLater(() -> LeftX.setProgress(gamepad.leftstickx / 100));
                    Platform.runLater(() -> LeftY.setProgress(gamepad.leftsticky / 100));
                    Platform.runLater(() -> LeftTriggerBar.setProgress(gamepad.lefttrigger / 100));

                    Platform.runLater(() -> RightX.setProgress(gamepad.rightstickx / 100));
                    Platform.runLater(() -> RightY.setProgress(gamepad.rightsticky / 100));
                    Platform.runLater(() -> RightTriggerBar.setProgress(gamepad.righttrigger / 100));


                    //these are for the controller visual feedback
                    controllerButtons(gamepad.ButtonX, ButtonX);
                    controllerButtons(gamepad.ButtonB, ButtonB);
                    controllerButtons(gamepad.ButtonA, ButtonA);
                    controllerButtons(gamepad.ButtonY, ButtonY);
                    controllerButtons(gamepad.ButtonRT, ButtonRT);
                    controllerButtons(gamepad.ButtonRB, ButtonRB);
                    controllerButtons(gamepad.ButtonLT, ButtonLT);
                    controllerButtons(gamepad.ButtonLB, ButtonLB);
                    controllerButtons(gamepad.ButtonUp, ButtonUp);
                    controllerButtons(gamepad.ButtonRight, ButtonRight);
                    controllerButtons(gamepad.ButtonDown, ButtonDown);
                    controllerButtons(gamepad.ButtonLeft, ButtonLeft);
                    controllerButtons(gamepad.ButtonRightStick, ButtonRightStick);
                    controllerButtons(gamepad.ButtonLeftStick, ButtonLeftStick);
                }
                Platform.runLater(() -> ControllerStatus.setText("Controller: Connected"));
            } else {
                Platform.runLater(() -> ControllerStatus.setText("Controller: Not Connected"));
            }
            if (!connected){
                Platform.runLater(() -> connectButton.setText("Connect"));
                Platform.runLater(() -> connectButton.setSelected(false));
                Platform.runLater(() -> ServerConnectionStatus.setText("Server: Not Connected"));
            } else if (connected){
                Platform.runLater(() -> connectButton.setText("Disconnect"));
                Platform.runLater(() -> connectButton.setSelected(true));
                Platform.runLater(() -> ServerConnectionStatus.setText("Server: Connected"));
            }
            if (!consoleTextField.getText().isEmpty()) {
                sendButton.setDisable(false);
            } else {
                sendButton.setDisable(true);
            }
            double RightPower = RightPowerSlider.getValue()-50;
            if (RightPower < 0) {
                Platform.runLater(() -> RightForwardIndicator.setProgress(-RightPower/50));
                Platform.runLater(() -> RightBackwardIndicator.setProgress(0));
            } else if (RightPower > 0) {
                Platform.runLater(() -> RightForwardIndicator.setProgress(0));
                Platform.runLater(() -> RightBackwardIndicator.setProgress(RightPower/50));
            } else {
                Platform.runLater(() -> RightForwardIndicator.setProgress(0));
                Platform.runLater(() -> RightBackwardIndicator.setProgress(0));
            }
            double LeftPower = LeftPowerSlider.getValue()-50;
            if (LeftPower < 0) {
                Platform.runLater(() -> LeftForwardIndicator.setProgress(-LeftPower/50));
                Platform.runLater(() -> LeftBackwardIndicator.setProgress(0));
            } else if (LeftPower > 0) {
                Platform.runLater(() -> LeftForwardIndicator.setProgress(0));
                Platform.runLater(() -> LeftBackwardIndicator.setProgress(LeftPower/50));
            } else {
                Platform.runLater(() -> LeftForwardIndicator.setProgress(0));
                Platform.runLater(() -> LeftBackwardIndicator.setProgress(0));
            }
            //Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Defines Output Stream
    private class Console extends OutputStream {

        private TextArea txtArea;
        File baseFile;

        public Console(TextArea txtArea) {
            this.txtArea = txtArea;
            baseFile = new File(OSUtils.osAppData()+"/log.log");
            if(!baseFile.exists()){
                try {
                    baseFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        @Override
        public void write(int b) throws IOException {
            Files.write(baseFile.toPath(),new String(new char[]{(char)b}).getBytes(),StandardOpenOption.APPEND);
            Platform.runLater(() -> txtArea.appendText(String.valueOf((char) b)));
            out.print(String.valueOf((char) b));
        }

    }
    public void redirectOutput(PrintStream printStream){
        System.setOut(printStream);
        System.setErr(printStream);
    }
    public static void disconnectServer(){
        connected = false;
    }/**/

}