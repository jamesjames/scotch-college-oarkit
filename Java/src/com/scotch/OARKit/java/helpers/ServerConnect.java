package com.scotch.OARKit.java.helpers;

import com.scotch.OARKit.java.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Campbell Millar on 5/03/2016.
 */
public class ServerConnect {
    //FOR DEFAULT CONNECTION
    private Socket socket;
    public ServerConnect(){
        if(Main.properties.getProperty("insideDev").equals("true")){
            try {
                socket = new Socket("localhost",5006);
                System.out.println("Starting Server on Local Host");
            } catch (IOException e) {
                System.err.println("Problem Connecting to Local Host - Is the server up?");
            }
        }else{
            try {
                socket = new Socket("192.168.100.1",5006);
                System.out.println("Starting Server on Default Remote");
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

    public void socketClose(){
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Problem Closing Socket - Is the server still up?");
        }
    }

    public boolean sendData(String data){
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            out.print(data);
            out.flush();
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for " + socket.getLocalAddress()+ " connection.");
            return false;
        }
        return false;
    }
}
