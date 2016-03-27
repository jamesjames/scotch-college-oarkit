package com.scotch.OARKit.java.helpers;

import java.io.InputStream;
import java.io.OutputStream;
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
    String stringFromFile;
    Map<String, String[]> codeMap = new HashMap<String, String[]>();
    public GetServerList(String filePath){
        this.filePath = filePath;
        openStream();
        stringFromFile = phaseStream();
        System.out.println(stringFromFile);
        phaseRaw();
        System.out.println(codeMap);
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
        }
    }
    public void saveString(){

    }
}
