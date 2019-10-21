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

}