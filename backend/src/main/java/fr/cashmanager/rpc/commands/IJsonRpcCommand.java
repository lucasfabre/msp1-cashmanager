package fr.cashmanager.rpc.commands;

import com.fasterxml.jackson.databind.JsonNode;

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
    public void parseParams(JsonNode params) throws Exception;

    /**
     * Execute the command
     * @return the result jsonNode
     * @throws Exception an exception in case of an error
     */
    public JsonNode execute() throws Exception;

    /**
     * factory method
     */
    public IJsonRpcCommand newInstance();
}