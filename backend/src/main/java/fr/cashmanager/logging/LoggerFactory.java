package fr.cashmanager.logging;

import fr.cashmanager.impl.ioc.ServicesContainer;

/**
 * LoggerFactory
 */
public class LoggerFactory {

    private ServicesContainer services;

    /**
     * default constructor
     * @param services
     */
    public LoggerFactory(ServicesContainer services) {
        this.services = services;
    }

    /**
     * create a new logger
     * @param the logger name
     * @return a logger
     */
    public Logger getLogger(String name) {
        return new Logger(services, name);
    }
}