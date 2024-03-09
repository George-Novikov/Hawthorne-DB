package com.georgen.hawthorne.settings;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.georgen.hawthorne.model.constants.ConfigProperty;
import com.georgen.hawthorne.model.constants.SystemProperty;
import com.georgen.hawthorne.model.exceptions.InitializationException;
import com.georgen.hawthorne.settings.yaml.YamlProperties;
import com.georgen.hawthorne.tools.paths.PathBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private Properties properties;
    private YamlProperties yamlProperties;

    public String getControlFilePath() throws IOException {
        String rootFolderName = getProperty(ConfigProperty.ROOT_PATH);
        String controlFileName = getProperty(ConfigProperty.CONTROL_FILE_NAME);

        if (rootFolderName == null) rootFolderName = ConfigProperty.ROOT_PATH.getDefaultValue();
        if (controlFileName == null) controlFileName = ConfigProperty.CONTROL_FILE_NAME.getDefaultValue();

        return PathBuilder.concatenate(rootFolderName, controlFileName);
    }

    public String getProperty(ConfigProperty property) throws IOException {
        Properties properties = initProperties();
        if (properties != null){
            return properties.getProperty(property.getName());
        }

        YamlProperties yamlProperties = initYamlProperties();
        if (yamlProperties != null){
            return yamlProperties.getProperty(property);
        }

        return null;
    }

    private Properties initProperties() throws IOException {
        if (this.properties == null || properties.isEmpty()){
            properties = new Properties();
            ClassLoader classLoader = this.getClass().getClassLoader();
            InputStream resourceStream = classLoader.getResourceAsStream(SystemProperty.APPLICATION_PROPERTIES_NAME.getValue());
            if (resourceStream == null) return null;
            properties.load(resourceStream);
        }
        return this.properties;
    }

    private YamlProperties initYamlProperties() throws IOException {
        if (this.yamlProperties == null || this.yamlProperties.isEmpty()){
            YAMLFactory yamlFactory = new YAMLFactory();
            ObjectMapper mapper = new ObjectMapper(yamlFactory);
            mapper.findAndRegisterModules();

            File yamlFile = new File(SystemProperty.APPLICATION_YAML_NAME.getValue());
            if (!yamlFile.exists()) yamlFile = new File(SystemProperty.APPLICATION_YML_NAME.getValue());
            if (!yamlFile.exists()) return null;

            this.yamlProperties = mapper.readValue(yamlFile, YamlProperties.class);
        }
        return this.yamlProperties;
    }
}
