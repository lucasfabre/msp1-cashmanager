package fr.cashmanager.rpc.middlewares;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.cashmanager.impl.helpers.JsonMapperFactory;
import fr.cashmanager.impl.ioc.ServicesContainer;
import fr.cashmanager.rpc.commands.IJsonRpcCommand;
import fr.cashmanager.rpc.commands.JsonRpcCommandManager;
import fr.cashmanager.rpc.exception.JsonRpcException;
import fr.cashmanager.rpc.exception.StandardJsonRpcErrorCode;
import fr.cashmanager.rpc.helpers.JsonRpcHelper;

/**
 * CommandMiddleware
 */
public class CommandMiddleware extends JsonRpcMiddleware {

	private ServicesContainer services;

	public CommandMiddleware(ServicesContainer services) {
		this.services = services;
	}

	@Override
	public JsonNode impl() throws JsonRpcException {
		JsonNode commandResult = parseAndExecuteCommand(body);
		JsonNode clientResult = formatClientResult(JsonRpcHelper.getId(body), commandResult);
		return clientResult;
	}
	
	/**
     * private implementation where we parse and execute a command
     * @param tree the parsed json object
     * @return the result jsonNode
     * @throws Exception if an exception occur durring the process
     */
    private JsonNode parseAndExecuteCommand(JsonNode tree) throws JsonRpcException {
        String method = JsonRpcHelper.getMethod(tree);
        if (method == null || JsonRpcHelper.getId(tree) < 1) {
            throw new JsonRpcException(StandardJsonRpcErrorCode.INVALID_REQUEST);
        }
        IJsonRpcCommand command = services.get(JsonRpcCommandManager.class).getCommandForMethod(method);
        if (command == null) {
            throw new JsonRpcException(StandardJsonRpcErrorCode.METHOD_NOT_FOUND);
        }
        command = command.newInstance();
        JsonNode params = JsonRpcHelper.getParams(tree);
        if (params == null) {
            throw new JsonRpcException(StandardJsonRpcErrorCode.INVALID_PARAMS);
        }
        command.parseParams(params); // throws an exception in case of an error
        return command.execute(); // throws an exception in case of an error
    }

    /**
     * format the result to a JSON-RPC result
     * @param id the id of the command
     * @param commandResult the result of the command
     * @return the formated JSON Object
     */
    private JsonNode formatClientResult(int id, JsonNode commandResult) {
        ObjectMapper mapper = JsonMapperFactory.getObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        result.put("jsonrpc", "2.0");
        result.put("id", id);
        result.set("result", commandResult);
        return result;
    }

}