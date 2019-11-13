package fr.cashmanager.rpc.middlewares;

import com.fasterxml.jackson.databind.JsonNode;

import fr.cashmanager.rpc.exception.JsonRpcException;
import fr.cashmanager.rpc.exception.StandardJsonRpcErrorCode;
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
            return JsonRpcHelper.formatClientError(getCommandIdOrNull(), e);
        } catch (Exception e) {
            return JsonRpcHelper.formatClientError(getCommandIdOrNull(), new JsonRpcException(StandardJsonRpcErrorCode.INTERNAL_ERROR));
        }
    }
    
    public Integer getCommandIdOrNull() {
        int commandId = JsonRpcHelper.getId(body); 
        return (commandId != 0) ? Integer.valueOf(commandId) : null;
    }
    
}