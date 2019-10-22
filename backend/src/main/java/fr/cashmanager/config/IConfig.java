package fr.cashmanager.config;

/**
 * IConfig
 * The interface used to init and configure the application and the differents services
 */
public interface IConfig {

    /**
     * Method called for the initialization of the application
     */
    public void configure() throws Exception;

    /**
     * return a configuration preference
     * @param name the name of the preference defined in Preference.java
     * @return the value or the default value of the preference
     */
    public String getPreference(Preference name);

}