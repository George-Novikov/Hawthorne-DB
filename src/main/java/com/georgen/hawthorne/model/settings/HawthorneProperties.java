package com.georgen.hawthorne.model.settings;

import com.georgen.hawthorne.model.constants.ConfigProperty;

public abstract class HawthorneProperties {
    public abstract String getProperty(ConfigProperty key);
    public abstract boolean isEmpty();
}
