package fr.cashmanager.rpc.commands;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import fr.cashmanager.rpc.middlewares.JsonRpcMiddleware;

/**
 * JsonRpcCommandManager
 */
public class JsonRpcCommandManager {
    
    private Map<String, IJsonRpcCommand> commandByMethod = new HashMap<String, IJsonRpcCommand>();
    private LinkedList<JsonRpcMiddleware> middlewareQueue = new LinkedList<JsonRpcMiddleware>();

    /**
     * Register a command in the server
     * @param command the command
     */
    public void registerCommand(IJsonRpcCommand command) {
        commandByMethod.put(command.getMethodName(), command);
    }

    /**
     * Register a middleware in the chain
     * @param middleware the middleware
     */
    public void registerMiddleware(JsonRpcMiddleware middleware) {
        middlewareQueue.addLast(middleware);
    }

    /**
     * Get the command associated to the method
     * @param method the method
     * @return the command
     */
    public IJsonRpcCommand getCommandForMethod(String method) {
        return commandByMethod.get(method);
    }


    /**
     * Return a new middleware queue
     */
    public Queue<JsonRpcMiddleware> getMiddlewareQueue() {
        return new LinkedList<JsonRpcMiddleware>(middlewareQueue);
    }

}