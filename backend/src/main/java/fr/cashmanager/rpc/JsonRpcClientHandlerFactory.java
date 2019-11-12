package fr.cashmanager.rpc;

import java.net.Socket;

import fr.cashmanager.impl.ioc.ServicesContainer;

/**
 * JsonRpcClientHandlerFactory
 */
public class JsonRpcClientHandlerFactory implements ClientHandlerFactory {
    
    private JsonRpcCommandManager commandManager;

    /**
     * default constructor
     * require: JsonRpcCommandManager
     * @param commandManager
     */
    public JsonRpcClientHandlerFactory(ServicesContainer container) {
        this.commandManager = container.get(JsonRpcCommandManager.class);
    }


    /**
     * client handler
     * @param socket the client socket
     */
    @Override
    public ClientHandler create(Socket socket) {
        return new JsonRpcClientHandler(commandManager, socket);
    }
} 
