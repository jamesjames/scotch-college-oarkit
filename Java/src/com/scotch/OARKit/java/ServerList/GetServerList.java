package com.scotch.OARKit.java.ServerList;

import com.scotch.OARKit.java.helpers.OSUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Campbell Millar on 24/03/2016.
 */
public class GetServerList {
    static File ServerListFile = new File(OSUtils.osAppData() + "/servers.sList");
    public String stringFromFile;
    String filePath;
    InputStream fileInputStream;
    OutputStream fileOutPutStream;
    Map<String, String[]> codeMap = new HashMap<String, String[]>();

    public GetServerList(String filePath) {
        this.filePath = filePath;
        try {
            if (ServerListFile.exists()) {
                stringFromFile = phaseFile();
            } else {
                openStream();
                stringFromFile = phaseStream();
                saveExtracted();
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        phaseRaw();
    }

    @Deprecated
    public static void saveServer(String name, String[] data) {
        String finalString = "\n" + name;
        for (String x : data) {
            finalString += ", " + x;
        }
        new ServerList(name, data);

        try {
            Files.write(ServerListFile.toPath(), finalString.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TO BE CALLED EACH TIME YOU UPDATE THE LIST - Auto done if using the Library
    public static void updateList() {
        String finalString = "Name, IP, Port\n";
        for (Map.Entry<String, ArrayList<Object>> entry : ServerList.array.entrySet()) {
            String key = entry.getKey();
            ArrayList<Object> value = entry.getValue();
            finalString += key + ", ";
            for (int i = 0; i < 2; i++) {
                finalString += (String) value.get(i);
                if (i != 1) finalString += ", ";
            }
            finalString += "\n";
        }
        try {
            Files.write(ServerListFile.toPath(), finalString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openStream() {
        fileInputStream = this.getClass().getClassLoader().getResourceAsStream(filePath);

    }

    private String phaseStream() {
        java.util.Scanner s = new java.util.Scanner(fileInputStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private String phaseFile() throws FileNotFoundException {
        java.util.Scanner s = new java.util.Scanner(new FileInputStream(ServerListFile)).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private void phaseRaw() {
        String arrayText[] = stringFromFile.split("\\r?\\n");
        for (int i = 1; i < arrayText.length; i++) {
            String v = arrayText[i];
            String x[] = v.split(", ");
            String[] temp = Arrays.copyOfRange(x, 1, x.length);
            codeMap.put(x[0], temp);
            new ServerList(x[0], temp);
        }
    }

    public void saveExtracted() throws IOException, NoSuchAlgorithmException {
        Files.write(ServerListFile.toPath(), stringFromFile.getBytes());
        FileInputStream fis = new FileInputStream(ServerListFile);
        /*if(OSUtils.getFileHash(fis).equals(OSUtils.getFileHash(fileInputStream))) {

        }*/
    }
}
