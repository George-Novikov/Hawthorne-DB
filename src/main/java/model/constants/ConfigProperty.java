package model.constants;

public enum ConfigProperty {
    MANAGING_FILE_NAME("hawthorne.storage.managing-file-name"),
    SETTINGS_FILE_NAME("hawthorne.storage.settings-file-name"),
    MAIN_DIRECTORY_NAME("hawthorne.storage.settings-file-name")
    ;

    private String name;

    ConfigProperty(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
