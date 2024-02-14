package com.georgen.hawthorne.config;

public class Settings {
    private SystemConfig config;

    public Settings(){
        this.config = new SystemConfig();
    }

    public SystemConfig getConfig() {
        return config;
    }

    public void setConfig(SystemConfig config) {
        this.config = config;
    }
}
