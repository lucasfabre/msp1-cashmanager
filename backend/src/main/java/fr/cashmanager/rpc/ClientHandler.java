package fr.cashmanager.rpc;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * ClientHandler
 */
abstract public class ClientHandler extends Thread {

    private Socket socket = null;

    /**
     * main constructor
     * @param socket the socket associated to the client to handle
     */
    ClientHandler(Socket socket) {
        this.socket = socket;
    }

    /**
     * Main method to implement in order to handle a client
     * @param is the socket inputStream
     * @param os the socket outputStream
     * @throws Exception
     */
    abstract public void handleClient(InputStream is, OutputStream os) throws Exception;

    /**
     * Main method for thread
     * handle the exceptions and throws a runtime exception
     */
    public void run() {
        try {
            this.handleClient(this.getSocket().getInputStream(),
                this.getSocket().getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException("Error when processing client", e);
        }
    }

    /**
     * @return the associated client socket
     */
    protected Socket getSocket() {
        return this.socket;
    }

}