package fr.cashmanager.rpc;

import java.net.Socket;

/**
 * JsonRpcClientHandlerFactory
 */
public class JsonRpcClientHandlerFactory implements ClientHandlerFactory {
    
    private JsonRpcCommandManager commandManager;

    /**
     * default constructor
     * @param commandManager
     */
    JsonRpcClientHandlerFactory(JsonRpcCommandManager commandManager) {
        this.commandManager = commandManager;
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
