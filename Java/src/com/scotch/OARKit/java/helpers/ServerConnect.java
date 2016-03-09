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

    public ServerConnect(){
        if(Main.properties.getProperty("insideDev").equals("true")){
            try {
                socket = new Socket("localhost",5006);
                System.out.println("Starting Server on Local Host");
                setUp();
            } catch (IOException e) {
                System.err.println("Problem Connecting to Local Host - Is the server up?");
            }
        }else{
            try {
                socket = new Socket("192.168.100.1",5006);
                System.out.println("Starting Server on Default Remote");
                setUp();
            } catch (IOException e) {
                System.err.println("Problem Connecting to Default Remote Host - Is the server up?");
            }
        }
    }
    //FOR DIFFERING IP BUT SAME PORT - 5006
    @Deprecated
    ServerConnect(String ip){

    }
    //FOR DIFFERING IP AND PORT
    @Deprecated
    ServerConnect(String ip, String port){

    }
    private void setUp() throws IOException {
         out = new PrintWriter(socket.getOutputStream(), true);
         in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         stdIn = new BufferedReader(new InputStreamReader(System.in));
    }

    public void socketClose(){
        try {
            socket.close();
        } catch (Exception e) {
            //System.err.println("Problem Closing Socket - Is the server still up?");
        }
    }

    public boolean sendData(String data){
        out.print(data);
        out.flush();
        return true;
    }
}
