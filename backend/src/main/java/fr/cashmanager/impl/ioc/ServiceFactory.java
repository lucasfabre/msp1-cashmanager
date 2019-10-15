package fr.cashmanager.impl.ioc;

/**
 * ServiceFactory
 * The service factory is used to register a method returning a service to the IOC container
 */
public interface ServiceFactory<T> {

    /**
     * the method returning the object
     * @return the service
     */
    public T instanciate();
}