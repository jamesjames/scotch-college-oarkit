package com.scotch.OARKit.java.ServerList;

import com.scotch.OARKit.java.ServerList.ServerList;
import com.scotch.OARKit.java.helpers.OSUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Campbell Millar on 24/03/2016.
 */
public class GetServerList {
    String filePath;
    InputStream fileInputStream;
    OutputStream fileOutPutStream;
    public String stringFromFile;
    Map<String, String[]> codeMap = new HashMap<String, String[]>();
    public GetServerList(String filePath){
        this.filePath = filePath;
        openStream();
        stringFromFile = phaseStream();
        try {
            saveExtracted();
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //System.out.println(stringFromFile);
        phaseRaw();
        //System.out.println(codeMap.get("test2"));
    }
    private void openStream(){
        fileInputStream = this.getClass().getClassLoader().getResourceAsStream(filePath);

    }
    private String phaseStream(){
        java.util.Scanner s = new java.util.Scanner(fileInputStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
    private void phaseRaw(){
        String arrayText[] = stringFromFile.split("\\r?\\n");
        for (int i = 1; i < arrayText.length; i++) {
            String v = arrayText[i];
            String x[] = v.split(", ");
            String[] temp = Arrays.copyOfRange(x, 1, x.length);
            codeMap.put(x[0],temp);
            new ServerList(x[0],temp);
        }
    }
    public static void saveServer(String name, String[] data)  {
        String finalString = "\n"+name;
        for (String x:data) {
            finalString += ", "+x;
        }
        new ServerList(name, data);
        File main = new File(OSUtils.osAppData()+"/servers.sList");
        try {
            Files.write(main.toPath(),finalString.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveExtracted() throws IOException, NoSuchAlgorithmException {
        File main = new File(OSUtils.osAppData()+"/servers.sList");
        if(!main.exists()){
            Files.write(main.toPath(),stringFromFile.getBytes());
        }
        FileInputStream fis = new FileInputStream(main);
        if(OSUtils.getFileHash(fis).equals(OSUtils.getFileHash(fileInputStream))) {

        }
    }
}
