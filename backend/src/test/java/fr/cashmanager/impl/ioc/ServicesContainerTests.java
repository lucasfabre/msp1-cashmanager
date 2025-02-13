package fr.cashmanager.impl.ioc;

import junit.framework.TestCase;

/**
 * ServicesContainerTests
 */
public class ServicesContainerTests extends TestCase {

    public void testSimpleRegister() {
        // create an instance
        Integer instance = Integer.valueOf(42);

        // register it
        ServicesContainer container = new ServicesContainer();
        container.register(Integer.class, instance);

        // assert
        Integer instanceFromContainer = container.get(Integer.class);
        assertEquals(Integer.valueOf(42), instanceFromContainer);
        assertTrue(instance == instanceFromContainer);
    }

    public void testSimpleNotFound() {
        ServicesContainer container = new ServicesContainer();
        try {
            container.get(Integer.class);
            assertTrue("the container does not throw a RuntimeException", false);
        } catch (RuntimeException e) {
            assertEquals("The service java.lang.Integer is not registered", e.getMessage());
        }
    }

    public void testSimpleFactory() {
        final StringBuilder value = new StringBuilder("il etait une fois...");

        // Create the factory
        ServiceFactory<String> factory = new ServiceFactory<String>() {
            public String instanciate() {
                return value.toString();
            }
        };

        // register it
        ServicesContainer container = new ServicesContainer();
        container.register(String.class, factory);

        // do some modification impacting the factory
        value.append("fin");

        // assert
        assertEquals("il etait une fois...fin", container.get(String.class));
    }

}