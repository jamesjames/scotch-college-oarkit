package com.scotch.OARKit.java.helpers;

import com.scotch.OARKit.java.Command.Commands;
import com.scotch.OARKit.java.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerConnect {
    //FOR DEFAULT CONNECTION
    private Socket socket;
    PrintWriter out;
    BufferedReader in;
    BufferedReader stdIn;
    public static boolean connected = false;
    @Deprecated
    public ServerConnect(){
        if(Main.properties.getProperty("insideDev").equals("true")){
            try {
                socket = new Socket("localhost",5006);
                System.out.println("Starting Server on Local Host");
                setUp();
            } catch (IOException e) {
                System.err.println("Problem Connecting to Local Host - Is the server up?");
                System.err.println("Please Specify Your Own IP");
            }
        }else{
            try {
                socket = new Socket("192.168.100.1",5006);
                System.out.println("Starting Server on Default Remote");
                setUp();
            } catch (IOException e) {
                System.err.println("Problem Connecting to Default Remote Host - Is the server up?");
                System.err.println("Please Specify Your Own IP");
            }
        }
        System.out.println("This method going to be removed, please use ServerConnect(String ip, int port)");
    }
    //FOR DIFFERING IP BUT SAME PORT - 5006
    public ServerConnect(String ip, String port){
        try {
            socket = new Socket(ip, Integer.parseInt(port));
            System.out.println("Connecting to Server...");
            setUp();
        } catch (IOException e) {
            System.err.println("Problem Connecting to Specified Host - Is the server up or is it configured correctly?");
        }
    }
    //FOR DIFFERING IP AND SAME PORT - 5006
    @Deprecated
    public ServerConnect(String ip){

    }
    private void setUp() throws IOException {
         out = new PrintWriter(socket.getOutputStream(), true);
         in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         stdIn = new BufferedReader(new InputStreamReader(System.in));
        connected = true;
    }

    public void socketClose(){
        try {
            socket.close();
            out.close();
            in.close();
            stdIn.close();
            connected = false;
        } catch (Exception e) {
            System.err.println("Problem Closing Socket - Is the server still up?");
        }
    }

    public boolean sendData(String data){
        if(connected){
            out.print(data);
            out.flush();
            return true;
        }else {
            System.err.println("Error - Socket is not connected :(");
            return false;
        }
    }
}
