package fr.cashmanager.rpc.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.cashmanager.impl.helpers.JsonMapperFactory;
import fr.cashmanager.rpc.exception.JsonRpcException;

/**
 * JsonRpcHelper
 */
public class JsonRpcHelper {

    /**
     * helper to get the method in the JSON-RPC request
     * @param command
     * @return the the method string
     */
    public static String getMethod(JsonNode command) {
        return command.path("method").asText();
    }

    /**
     * helper to get the params in the JSON-RPC request
     * @param command
     * @return the the params jsonNode
     */
    public static JsonNode getParams(JsonNode command) {
        return command.get("params");
    }

    /**
     * helper to get the id in the JSON-RPC request
     * @param command
     * @return the the id
     */
    public static int getId(JsonNode command) {
        return command.path("id").asInt();
    }

    /**
     * helper to get the id in the JSON-RPC request
     * @param command
     * @return the the id or null
     */
    public static Integer getIdOrNull(JsonNode command) {
        int id = command.path("id").asInt();
        return (id != 0) ? Integer.valueOf(id) : null;
    }

    /**
     * format the error to a JSON-RPC error
     * @param e the exception
     * @return the formated JSON Object
     */
    public static JsonNode formatClientError(Integer id, JsonRpcException e) {
        ObjectMapper mapper = JsonMapperFactory.getObjectMapper();
        ObjectNode errorNode = mapper.createObjectNode();
        errorNode.put("code", e.getCode());
        errorNode.put("message", e.getMessage());
        ObjectNode result = mapper.createObjectNode();
        result.put("jsonrpc", "2.0");
        if (id != null) {
            result.put("id", id);
        }
        result.set("error", errorNode);
        return result;
    }

    /**
     * format the result to a JSON-RPC result
     * @param id the id of the command
     * @param commandResult the result of the command
     * @return the formated JSON Object
     */
    public static JsonNode formatClientResult(Integer id, JsonNode commandResult) {
        ObjectMapper mapper = JsonMapperFactory.getObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        result.put("jsonrpc", "2.0");
        if (id != null) {
            result.put("id", id);
        }
        result.set("result", commandResult);
        return result;
    }
}