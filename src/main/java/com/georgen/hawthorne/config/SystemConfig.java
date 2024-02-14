package com.georgen.hawthorne.config;


import com.georgen.hawthorne.model.constants.ConfigPropertyName;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class SystemConfig {
    private static final String CONFIG_FILE_NAME = "hawthorne.properties";
    private Properties properties;

    public String getControlFilePath() throws IOException {
        String rootFolderName = getProperty(ConfigPropertyName.ROOT_DIRECTORY_NAME);
        String controlFileName = getProperty(ConfigPropertyName.CONTROL_FILE_NAME);
        return String.format("%s%s%s", rootFolderName, File.separator, controlFileName);
    }

    public String getProperty(ConfigPropertyName property) throws IOException {
        Properties properties = initConfigFile();
        return properties.getProperty(property.getName());
    }

    private Properties initConfigFile() throws IOException {
        properties = new Properties();
        ClassLoader classLoader = this.getClass().getClassLoader();
        properties.load(classLoader.getResourceAsStream(CONFIG_FILE_NAME));
        return properties;
    }
}
