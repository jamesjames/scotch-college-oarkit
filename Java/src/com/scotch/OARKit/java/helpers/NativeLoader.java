package com.scotch.OARKit.java.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Campbell Millar/ZENOTON - LICENCED UNDER LGPL.
 */
public class NativeLoader {
    static String tempdir;
    public static void loadLib(String name) throws IOException {
        String os=System.getProperty("os.name").toLowerCase();

        InputStream in = NativeLoader.class.getResourceAsStream(name);
        byte[] buffer = new byte[1024];
        int read = -1;
        File temp = new File(new File(System.getProperty("java.io.tmpdir")), name.replace("/",""));
        System.out.println(temp);
        FileOutputStream fos = new FileOutputStream(temp);

        while((read = in.read(buffer)) != -1) {
            fos.write(buffer, 0, read);
        }
        fos.close();
        in.close();
        if (os.contains("win")){
            String tempname = name.replace("/","\\");
            //DEBUG INFO - NOT NEEDED
            //System.out.println(tempname);
            //System.out.println("Loaded Link " + name + " OS is " + os);
            tempdir = temp.toString().replace(tempname,"");
        }else{
            //System.out.println("Loaded Link " + name + " OS is " + os);
            tempdir = temp.toString().replace(name,"");
            //System.out.println(tempdir);
        }
        System.setProperty("java.library.path", tempdir);
    }
}
