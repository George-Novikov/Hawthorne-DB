package com.georgen.hawthorne.settings.yaml;

import com.georgen.hawthorne.model.constants.ConfigProperty;

public class YamlProperties {
    private HawthorneNode hawthorne;

    public HawthorneNode getHawthorne() {
        return hawthorne;
    }

    public void setHawthorne(HawthorneNode hawthorne) {
        this.hawthorne = hawthorne;
    }

    public String getProperty(ConfigProperty property){
        if (hawthorne == null || hawthorne.isEmpty()) return null;
        NamingNode naming = hawthorne.getNaming();

        switch (property){
            case CONTROL_FILE_NAME:
                return naming.getControlFile();
            case ROOT_PATH:
                return naming.getRootDirectory();
            case ENTITIES_PATH:
                return naming.getEntitiesDirectory();
            default:
                return null;
        }
    }

    public boolean isEmpty(){
        return hawthorne == null || hawthorne.isEmpty();
    }
}
