package com.scotch.OARKit.java.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;

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
        //Logger.info(temp);
        FileOutputStream fos = new FileOutputStream(temp);

        while((read = in.read(buffer)) != -1) {
            fos.write(buffer, 0, read);
        }
        fos.close();
        in.close();
        if (os.contains("win")){
            String tempname = name.replace("/","\\");
            //DEBUG INFO - NOT NEEDED
            //Logger.info(tempname);
            //Logger.info("Loaded Link " + name + " OS is " + os);
            tempdir = temp.toString().replace(tempname,"");
        }else{
            //Logger.info("Loaded Link " + name + " OS is " + os);
            tempdir = temp.toString().replace(name,"");
            //Logger.info(tempdir);
        }
        //Logger.info(tempdir);
        //System.setProperty("Djava.library.path", tempdir);
        try {
            addLibraryPath(tempdir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void addLibraryPath(String pathToAdd) throws Exception{
        final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
        usrPathsField.setAccessible(true);

        //get array of paths
        final String[] paths = (String[])usrPathsField.get(null);

        //check if the path to add is already present
        for(String path : paths) {
            if(path.equals(pathToAdd)) {
                return;
            }
        }

        //add the new path
        final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
        newPaths[newPaths.length-1] = pathToAdd;
        usrPathsField.set(null, newPaths);
    }
}
