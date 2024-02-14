package com.georgen.hawthorne.config;

public class SettingsContainer {
    private static Settings instance;

    public static synchronized Settings getInstance(){
        if (instance == null){
            synchronized (SettingsContainer.class){
                if (instance == null){
                    instance = new Settings();
                }
            }
        }
        return instance;
    }
}
