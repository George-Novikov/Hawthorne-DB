package com.georgen.hawthorne.settings;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.georgen.hawthorne.model.constants.ConfigProperty;
import com.georgen.hawthorne.model.constants.SystemProperty;
import com.georgen.hawthorne.tools.PathBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private Properties properties;
    private static final YAMLFactory YAML_FACTORY = new YAMLFactory();
    private static final ObjectMapper YAML_MAPPER = new ObjectMapper(YAML_FACTORY);

    public String getControlFilePath() throws IOException {
        String rootFolderName = getProperty(ConfigProperty.ROOT_PATH);
        String controlFileName = getProperty(ConfigProperty.CONTROL_FILE_NAME);
        return PathBuilder.concatenate(rootFolderName, controlFileName);
    }

    public String getProperty(ConfigProperty property) throws IOException {
        Properties properties = initProperties();
        return properties.getProperty(property.getName());
    }

    private Properties initProperties() throws IOException {
        if (this.properties == null || properties.isEmpty()){
            properties = new Properties();
            ClassLoader classLoader = this.getClass().getClassLoader();
            InputStream resourceStream = classLoader.getResourceAsStream(SystemProperty.APPLICATION_PROPERTIES_NAME.getValue());
            properties.load(resourceStream);
        }
        return this.properties;
    }
}
