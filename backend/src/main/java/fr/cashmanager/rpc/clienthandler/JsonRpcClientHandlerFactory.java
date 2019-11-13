package fr.cashmanager.rpc.clienthandler;

import java.net.Socket;

import fr.cashmanager.impl.ioc.ServicesContainer;
import fr.cashmanager.rpc.commands.JsonRpcCommandManager;

/**
 * JsonRpcClientHandlerFactory
 */
public class JsonRpcClientHandlerFactory implements ClientHandlerFactory {
    
    private JsonRpcCommandManager commandManager;
    private ServicesContainer services;

    /**
     * default constructor
     * require: JsonRpcCommandManager
     * @param commandManager
     */
    public JsonRpcClientHandlerFactory(ServicesContainer services) {
        this.commandManager = services.get(JsonRpcCommandManager.class);
        this.services = services;
    }


    /**
     * client handler
     * @param socket the client socket
     */
    @Override
    public ClientHandler create(Socket socket) {
        return new JsonRpcClientHandler(services, commandManager, socket);
    }
} 
