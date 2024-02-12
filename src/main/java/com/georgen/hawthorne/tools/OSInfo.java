package com.georgen.hawthorne.tools;

public class OSInfo {
    public static boolean isUnixSystem(){
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.contains("nix") || osName.contains("nux");
    }
}
