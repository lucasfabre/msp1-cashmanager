package fr.cashmanager.rpc.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import fr.cashmanager.config.IConfig;
import fr.cashmanager.config.Preference;
import fr.cashmanager.impl.ioc.ServicesContainer;
import fr.cashmanager.logging.Logger;
import fr.cashmanager.logging.LoggerFactory;
import fr.cashmanager.rpc.clienthandler.ClientHandler;
import fr.cashmanager.rpc.clienthandler.ClientHandlerFactory;

/**
 * SocketServer
 */
public class SocketServer implements IServer {

    private ServicesContainer services;
    private Logger log;

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
    public SocketServer(ServicesContainer services) throws IllegalArgumentException {
        this.services = services;
        this.log = services.get(LoggerFactory.class).getLogger("SocketServer");
    }

    public void listen() throws IOException {
        ClientHandlerFactory clientFactory = services.get(ClientHandlerFactory.class);
        int port = this.getPort();
        this.serverSocket = new ServerSocket(port);
        this.isRunning = true;
        log.info("Server started on port " + Integer.valueOf(port));
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
        IConfig config = services.get(IConfig.class);
        try {
            return Integer.parseInt(config.getPreference(Preference.SERVER_PORT));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error while parsing the server port in the configuration", e);
        }
    }
}