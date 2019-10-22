package fr.cashmanager.rpc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import fr.cashmanager.config.IConfig;
import fr.cashmanager.config.Preference;

/**
 * SocketServer
 */
public class SocketServer implements IServer {

    // services
    ClientHandlerFactory clientFactory;

    // attributes
    private int port;
    boolean isRunning = false;
    ServerSocket serverSocket = null;

    /**
     * init the server with the diferents services
     * @param config the config
     * @param clientFactory the clientfactory
     * @throws IllegalArgumentException
     */
    SocketServer(IConfig config, ClientHandlerFactory clientFactory) throws IllegalArgumentException {
        this.clientFactory = clientFactory;
        try {
            this.port = Integer.parseInt(config.getPreference(Preference.SERVER_PORT));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error while parsing the server port in the configuration", e);
        }
    }

    public void listen() throws IOException {
        this.serverSocket = new ServerSocket(this.port);
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
}