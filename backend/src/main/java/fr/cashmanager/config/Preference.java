package fr.cashmanager.config;

/**
 * Preferences
 */
public enum Preference {

    SERVER_PORT("server.port", "8080");

    private String name;
    private String defaultValue;
 
    /**
     * default constructor
     * @param name
     * @param defaultValue
     */
    Preference(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }
 
    /**
     * get the name of the preference
     * @return the name of the preference
     */
    public String getName() {
        return this.name;
    }

    /**
     * get the default value of the preference
     * @return default value
     */
    public String getDefaultValue() {
        return this.defaultValue;
    }
}