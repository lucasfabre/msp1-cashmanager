package fr.cashmanager.rpc;

import java.net.Socket;

/**
 * ClientHandlerFactory
 */
public interface ClientHandlerFactory {

    /**
     * create a clientHandler
     * @return a new ClientHandler
     */
    public ClientHandler create(Socket socket);

}