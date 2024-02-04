package config;


import model.constants.ConfigProperty;

import java.io.IOException;
import java.util.Properties;

public class Configuration {
    private static final String CONFIG_FILE_NAME = "hawthorne.properties";

    public String readProperty(ConfigProperty property) throws IOException {
        Properties properties = getConfigFile();
        return properties.getProperty(property.getName());
    }

    private Properties getConfigFile() throws IOException {
        Properties properties = new Properties();
        ClassLoader classLoader = this.getClass().getClassLoader();
        properties.load(classLoader.getResourceAsStream(CONFIG_FILE_NAME));
        return properties;
    }
}
