package com.scotch.OARKit.java.helpers;

import java.io.File;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Campbell Millar on 27/03/2016.
 */
public class OSUtils {
    public static String osTemp() {
        return System.getProperty("java.io.tmpdir");
    }

    public static String osAppData() {
        String OS = (System.getProperty("os.name")).toUpperCase();
        if (OS.contains("WIN")) OS = System.getenv("AppData") + "\\Scotch\\OARKit";
        else if (OS.contains("MAC"))
            OS = System.getProperty("user.home") + "/Library/Application Support" + "/Scotch/OARKit";
        else OS = System.getProperty("user.home") +"/.Scotch/OARKit";
        File pathFile = new File(OS);
        if (!pathFile.exists()) pathFile.mkdirs();
        return OS;
    }

    public static String getFileHash(InputStream in) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        DigestInputStream x = new DigestInputStream(in, md);
        String hash = "";
        for (byte b : md.digest()) {
            hash += b;
        }
        return hash;
    }
}
