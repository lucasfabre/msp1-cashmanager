package fr.cashmanager.impl.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JsonMapperFactory
 */
public class JsonMapperFactory {

    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * the JsonObjectMappers are slow to instanciate so
     * we create one for the entire application
     */
    public static ObjectMapper getObjectMapper() {
        return mapper;
    }
}