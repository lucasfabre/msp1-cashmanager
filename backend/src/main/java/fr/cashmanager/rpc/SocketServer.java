package fr.cashmanager.rpc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import fr.cashmanager.config.IConfig;
import fr.cashmanager.config.Preference;
import fr.cashmanager.impl.ioc.ServicesContainer;

/**
 * SocketServer
 */
public class SocketServer implements IServer {

    // services
    ClientHandlerFactory clientFactory;
    IConfig config;

    // attributes
    boolean isRunning = false;
    ServerSocket serverSocket = null;

    /**
     * init the server with the diferents services
     * require: IConfig, ClientHandlerFactory
     * @param config the config
     * @param clientFactory the clientfactory
     * @throws IllegalArgumentException
     */
    public SocketServer(ServicesContainer container) throws IllegalArgumentException {
        this.config = container.get(IConfig.class);
        this.clientFactory = container.get(ClientHandlerFactory.class);
    }

    public void listen() throws IOException {
        int port = this.getPort();
        this.serverSocket = new ServerSocket(port);
        this.isRunning = true;
        while (this.isRunning) {
            try {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = clientFactory.create(socket);
                clientHandler.start();
            } catch (SocketException socketException) {
                // the only SocketException throw here is when the serverSocket is closed
                // so for now we want to fail silently here
                this.isRunning = false;
            }
        }
    }

    /**
     * Is the server launched
     * @return true if the server is running
     */
    @Override
    public boolean isRunning() {
        return this.isRunning;
    }

    /**
     * Stop the server
     */
    @Override
    public void stop() throws IOException {
        serverSocket.close();
    }

    private int getPort() {
        try {
            return Integer.parseInt(config.getPreference(Preference.SERVER_PORT));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error while parsing the server port in the configuration", e);
        }
    }
}