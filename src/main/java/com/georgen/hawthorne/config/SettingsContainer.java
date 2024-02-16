package com.georgen.hawthorne.config;

public class SettingsContainer {
    private static Settings instance;

    public static Settings getInstance(){
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
