package fr.cashmanager.logging;

import fr.cashmanager.impl.ioc.ServicesContainer;

/**
 * Logger
 */
public class Logger {

    private String name;
    private ServicesContainer services;

    /**
     * default constructor
     * @param services the service container
     * @param name the name of the logger
     */
    Logger(ServicesContainer services, String name) {
        this.name = name;
        this.services = services;
    }

    /**
     * log a debug message
     */
    public void debug(String message) {
        this.debug(message, null);
    }

    /**
     * log a debug message with exception
     */
    public void debug(String message, Exception e) {
        services.get(LoggerService.class).write(LogLevel.DEBUG, this.name, message, e);
    }
    
    /**
     * log a info message
     */
    public void info(String message) {
        this.info(message, null);
    }

    /**
     * log a info message with exception
     */
    public void info(String message, Exception e) {
        services.get(LoggerService.class).write(LogLevel.INFO, this.name, message, e);
    }

    /**
     * log a warning message
     */
    public void warn(String message) {
        this.warn(message, null);
    }

    /**
     * log a warning message with exception
     */
    public void warn(String message, Exception e) {
        services.get(LoggerService.class).write(LogLevel.WARN, this.name, message, e);
    }

    /**
     * log a error message
     */
    public void error(String message) {
        this.error(message, null);
    }

    /**
     * log a error message with exception
     */
    public void error(String message, Exception e) {
        services.get(LoggerService.class).write(LogLevel.ERROR, this.name, message, e);
    }

}