package fr.cashmanager.rpc.middlewares;

import java.util.Map;
import java.util.Queue;

import com.fasterxml.jackson.databind.JsonNode;

import fr.cashmanager.rpc.exception.JsonRpcException;

/**
 * JsonRpcMiddleware
 * base class for any JsonRpcMiddleware 
 */
public abstract class JsonRpcMiddleware {

    protected Queue<JsonRpcMiddleware> queue;
    protected Map<String, Object> session;
    protected JsonNode body;

    /**
     * actual middleware implementation
     * @return
     * @throws JsonRpcException
     */
    public abstract JsonNode impl() throws JsonRpcException;

    /**
     * run the middleware chain
     * @param queue the middleware queue
     * @param session the session
     * @param body the body of the request
     * @return the response
     * @throws JsonRpcException
     */
    public JsonNode run(Queue<JsonRpcMiddleware> queue, Map<String, Object> session, JsonNode body) throws JsonRpcException {
        this.queue = queue;
        this.session = session;
        this.body = body;
        return impl();
    }

    /**
     * call the next middleware in the chain
     * @return the result of the middleware (null if no next middleware)
     * @throws JsonRpcException
     */
    protected JsonNode next() throws JsonRpcException {
        if (queue.isEmpty() == false) {
            JsonRpcMiddleware next = queue.poll();
            return next.run(queue, session, body);
        }
        return null; // if no middleware respond
    }

}