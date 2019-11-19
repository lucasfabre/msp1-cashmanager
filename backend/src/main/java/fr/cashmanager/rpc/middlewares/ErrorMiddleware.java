package fr.cashmanager.rpc.middlewares;

import com.fasterxml.jackson.databind.JsonNode;

import fr.cashmanager.rpc.exception.JsonRpcException;
import fr.cashmanager.impl.ioc.ServicesContainer;
import fr.cashmanager.logging.Logger;
import fr.cashmanager.logging.LoggerFactory;
import fr.cashmanager.rpc.exception.JsonRpcErrorCode;
import fr.cashmanager.rpc.helpers.JsonRpcHelper;

/**
 * ErrorMiddleware
 * Handle and format the errors in the chain
 */
public class ErrorMiddleware extends JsonRpcMiddleware {

    private Logger log;

    public ErrorMiddleware(ServicesContainer services) {
        this.log = services.get(LoggerFactory.class).getLogger("ErrorMiddleware");
    }

    /**
     * Format any Exception in a JsonRpc compatible error 
     */
    @Override
	public JsonNode impl() throws JsonRpcException {
		try {
            return next();
        } catch (JsonRpcException e) {
            return JsonRpcHelper.formatClientError(JsonRpcHelper.getIdOrNull(body), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return JsonRpcHelper.formatClientError(JsonRpcHelper.getIdOrNull(body), new JsonRpcException(JsonRpcErrorCode.INTERNAL_ERROR));
        }
    }
    
}