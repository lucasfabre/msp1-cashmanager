package fr.cashmanager.rpc.server;

import java.io.IOException;

/**
 * IServer
 */
public interface IServer {

    /**
     * listen on the configured port
     * @throws IOException
     */
    public void listen() throws IOException;

    /**
     * Is the server launched
     * @return true if the server is running
     */
    public boolean isRunning();

    /**
     * stop the listening
     */
    public void stop() throws IOException;
}