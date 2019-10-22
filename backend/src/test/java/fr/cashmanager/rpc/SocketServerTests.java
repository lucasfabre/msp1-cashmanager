package fr.cashmanager.rpc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import fr.cashmanager.config.IConfig;
import fr.cashmanager.config.Preference;
import junit.framework.TestCase;

/**
 * SocketServerTests
 */
public class SocketServerTests extends TestCase {

    public final int SERVER_PORT = 3811;

    public IConfig config;
    public ClientHandlerFactory clientHandlerFactory;
    public static IServer server;

    static class SimpleSocketTestClient extends ClientHandler {
        SimpleSocketTestClient(Socket socket) {
            super(socket);
        }

        @Override
        public void handleClient(InputStream is, OutputStream os) throws Exception {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            OutputStreamWriter writer = new OutputStreamWriter(os);
            assertEquals("ping", reader.readLine());
            writer.write("pong\n");
            writer.flush();
        }
    }

    @Override
    protected void setUp() throws Exception {
        // IConfig
        this.config = new IConfig() {
            Map<String, String> preference = new HashMap<String, String>();

            @Override
            public String getPreference(Preference preference) {
                return this.preference.get(preference.getName());
            }

            @Override
            public void configure() throws Exception {
                preference.put(Preference.SERVER_PORT.getName(), Integer.valueOf(SERVER_PORT).toString());
            }
        };
        // ClientHandlerFactory
        this.clientHandlerFactory = new ClientHandlerFactory() {
            @Override
            public ClientHandler create(Socket socket) {
                return new SimpleSocketTestClient(socket);
            }
        };
        this.config.configure();
        server = new SocketServer(this.config, this.clientHandlerFactory);
    }

    @Test(timeout = 10000)
    public void testSocket() throws Exception {
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
        // Client
        Socket socket = new Socket("localhost", SERVER_PORT);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
        writer.write("ping\n");
        writer.flush();
        assertEquals("pong", reader.readLine());
        socket.close();
        server.stop();
    }
    
}