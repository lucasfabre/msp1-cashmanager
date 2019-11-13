package fr.cashmanager.integration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import fr.cashmanager.config.LocalFileConfig;
import fr.cashmanager.impl.ioc.ServicesContainer;
import fr.cashmanager.rpc.server.IServer;

/**
 * IntegrationTestBase
 */
public abstract class IntegrationTestBase {
    
    public static class MockedLocalFileConfig extends LocalFileConfig {
        public MockedLocalFileConfig(ServicesContainer services) {
            super(services);
        }

        public void setPreference(String name, String value) {
            this.preferences.put(name, value);
        }
    }

    protected ServicesContainer services = new ServicesContainer();
    protected Socket clientSocket;
    protected BufferedReader clientReader;
    protected OutputStreamWriter clientWriter;
    
    abstract public void setUpAppContext() throws Exception;

    protected void waitForServerStarted() throws Exception {
        IServer server = services.get(IServer.class);
        Thread serverThread = new Thread() {
            @Override
            public void run() {
                try {
                    server.listen();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        serverThread.start();
        while (server.isRunning() == false) {
            Thread.sleep(500);
        }
    }

    protected void closeConectionAndStopServer() throws IOException {
        IServer server = services.get(IServer.class);
        clientSocket.close();
        server.stop();
    }

    protected void initClientConection(int port) throws IOException {
        this.clientSocket = new Socket("localhost", port);
        this.clientReader = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        this.clientWriter = new OutputStreamWriter(this.clientSocket.getOutputStream());
    }

    protected String writeMessageAndWaitResponse(String message) throws IOException {
        clientWriter.write(message + "\n");
        clientWriter.flush();
        return clientReader.readLine();
    }
}