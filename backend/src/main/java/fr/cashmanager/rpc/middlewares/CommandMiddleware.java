package fr.cashmanager.rpc.middlewares;

import com.fasterxml.jackson.databind.JsonNode;

import fr.cashmanager.impl.ioc.ServicesContainer;
import fr.cashmanager.rpc.commands.IJsonRpcCommand;
import fr.cashmanager.rpc.commands.JsonRpcCommandManager;
import fr.cashmanager.rpc.exception.JsonRpcException;
import fr.cashmanager.rpc.exception.JsonRpcErrorCode;
import fr.cashmanager.rpc.helpers.JsonRpcHelper;

/**
 * CommandMiddleware
 */
public class CommandMiddleware extends JsonRpcMiddleware {

	private ServicesContainer services;

	public CommandMiddleware(ServicesContainer services) {
		this.services = services;
	}

    /**
     * Execute the registered matching command
     */
	@Override
	public JsonNode impl() throws JsonRpcException {
		JsonNode commandResult = parseAndExecuteCommand(body);
		JsonNode clientResult = JsonRpcHelper.formatClientResult(JsonRpcHelper.getId(body), commandResult);
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
            throw new JsonRpcException(JsonRpcErrorCode.INVALID_REQUEST);
        }
        IJsonRpcCommand command = services.get(JsonRpcCommandManager.class).getCommandForMethod(method);
        if (command == null) {
            throw new JsonRpcException(JsonRpcErrorCode.METHOD_NOT_FOUND);
        }
        command = command.newInstance();
        JsonNode params = JsonRpcHelper.getParams(tree);
        if (params == null) {
            throw new JsonRpcException(JsonRpcErrorCode.INVALID_PARAMS);
        }
        command.parseParams(params); // throws an exception in case of an error
        return command.execute(session); // throws an exception in case of an error
    }

}