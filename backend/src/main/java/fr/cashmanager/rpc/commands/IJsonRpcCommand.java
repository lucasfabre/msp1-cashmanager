package fr.cashmanager.rpc.commands;

import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;

import fr.cashmanager.rpc.exception.JsonRpcException;

/**
 * JsonRpcCommand
 */
public interface IJsonRpcCommand {
    
    /**
     * Get the command method name
     * @return the command method name
     */
    public String getMethodName();

    /**
     * Parse the command arguments
     * @param params the json containing the arguments
     * @throws Exception a error in case of the arguments are false
     */
    public void parseParams(JsonNode params) throws JsonRpcException;

    /**
     * Execute the command
     * @return the result jsonNode
     * @param session the connection session
     * @throws Exception an exception in case of an error
     */
    public JsonNode execute(Map<String, Object> session) throws JsonRpcException;

    /**
     * factory method
     */
    public IJsonRpcCommand newInstance();
}