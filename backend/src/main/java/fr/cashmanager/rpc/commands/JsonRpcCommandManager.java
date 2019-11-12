package fr.cashmanager.rpc.commands;

import java.util.HashMap;
import java.util.Map;

/**
 * JsonRpcCommandManager
 */
public class JsonRpcCommandManager {
    
    private Map<String, IJsonRpcCommand> commandByMethod = new HashMap<String, IJsonRpcCommand>();

    /**
     * Register a command in the server
     * @param command the command
     */
    public void registerCommand(IJsonRpcCommand command) {
        commandByMethod.put(command.getMethodName(), command);
    }

    /**
     * Get the command associated to the method
     * @param method the method
     * @return the command
     */
    public IJsonRpcCommand getCommandForMethod(String method) {
        return commandByMethod.get(method);
    }
}