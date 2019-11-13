package fr.cashmanager.rpc.middlewares;

import com.fasterxml.jackson.databind.JsonNode;

import fr.cashmanager.rpc.exception.JsonRpcException;
import fr.cashmanager.rpc.helpers.JsonRpcHelper;

/**
 * ErrorMiddleware
 * Handle and format the errors in the chain
 */
public class ErrorMiddleware extends JsonRpcMiddleware {

	@Override
	public JsonNode impl() throws JsonRpcException {
		try {
            return next();
        } catch (JsonRpcException e) {
            int commandId = JsonRpcHelper.getId(body); 
            return JsonRpcHelper.formatClientError(
                (commandId != 0) ? Integer.valueOf(commandId) : null, e);
        }
	}
    
}