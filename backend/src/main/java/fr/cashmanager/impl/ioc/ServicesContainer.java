package fr.cashmanager.impl.ioc;

import java.util.HashMap;
import java.util.Map;

/**
 * ServicesContainer 
 * Minimalist IOC container for the cash manager
 */
public class ServicesContainer {

    
    private Map<Class<?>, Object> container = new HashMap<Class<?>, Object>();

    /**
     * register an instance to the service container
     * @param <T> the type of the instance
     * @param clazz the class of the instance
     * @param service the instance
     */
    public <T> void register(Class<T> clazz, T service) {
        container.put(clazz, service);
    }
    
    /**
     * Register a function as a factoy used by the IOC container to generate instances
     * @param <T> the type of the instance
     * @param clazz the class of the instance
     * @param factory the ServiceFactory called when retrieving an instance
     */
    public <T> void register(Class<T> clazz, ServiceFactory<T> factory) {
        container.put(clazz, factory);
    }

    /**
     * Get the associated instance
     * @param <T> the type of the instance
     * @param clazz the class of the instance
     * @return a ready to use instance
     */
    public <T> T get(Class<T> clazz) {
        T result;
        Object value = container.get(clazz);
        if (value instanceof ServiceFactory) {
            ServiceFactory<?> factory = (ServiceFactory<?>) value;
            value = factory.instanciate();
        }
        result = clazz.cast(value);
        return result;
    }
}