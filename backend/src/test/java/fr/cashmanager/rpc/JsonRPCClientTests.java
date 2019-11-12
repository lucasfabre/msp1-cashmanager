package fr.cashmanager.rpc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;

import fr.cashmanager.config.IConfig;
import fr.cashmanager.config.Preference;
import fr.cashmanager.impl.helpers.JsonMapperFactory;
import fr.cashmanager.impl.ioc.ServicesContainer;
import fr.cashmanager.rpc.clienthandler.ClientHandlerFactory;
import fr.cashmanager.rpc.clienthandler.JsonRpcClientHandlerFactory;
import fr.cashmanager.rpc.commands.IJsonRpcCommand;
import fr.cashmanager.rpc.commands.JsonRpcCommandManager;
import fr.cashmanager.rpc.server.IServer;
import fr.cashmanager.rpc.server.SocketServer;
import junit.framework.TestCase;

/**
 * SocketServerTests
 */
public class JsonRPCClientTests extends TestCase {

    public final int SERVER_PORT = 3812;

    public IConfig config;
    public ClientHandlerFactory clientHandlerFactory;
    public static IServer server;

    class SubstractCommand implements IJsonRpcCommand {
        private List<Integer> params = null;
        @Override
        public void parseParams(JsonNode params) throws Exception {
            ObjectMapper mapper = JsonMapperFactory.getObjectMapper();
            JavaType jt = mapper.getTypeFactory().constructType(new TypeReference<List<Integer>>(){});
            this.params = mapper.readValue(mapper.treeAsTokens(params), jt);
        }

        @Override
        public String getMethodName() {
            return "subtract";
        }

        @Override
        public JsonNode execute() throws Exception {
            ObjectMapper mapper = JsonMapperFactory.getObjectMapper();
            Integer result = params.stream().reduce((a, b) -> a - b).get();
            ObjectNode commandResult = mapper.createObjectNode();
            commandResult.put("value", result);
            return commandResult;
        }

        @Override
        public IJsonRpcCommand newInstance() {
            try {
                return new SubstractCommand();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void setUp() throws Exception {
        ServicesContainer container = new ServicesContainer();
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
        container.register(IConfig.class, this.config);
        // JsonRpcCommandManager
        JsonRpcCommandManager jsonRpcCommandManager = new JsonRpcCommandManager();
        jsonRpcCommandManager.registerCommand(new SubstractCommand());
        container.register(JsonRpcCommandManager.class, jsonRpcCommandManager);
        // ClientHandlerFactory
        this.clientHandlerFactory = new JsonRpcClientHandlerFactory(container);
        container.register(ClientHandlerFactory.class, this.clientHandlerFactory);
        // IServer
        server = new SocketServer(container);
        container.register(IServer.class, server);
        // init
        this.config.configure();
    }

    @Test(timeout = 10000)
    public void testSocket() throws Exception {
        ObjectMapper mapper = JsonMapperFactory.getObjectMapper();
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
        writer.write("{\"jsonrpc\": \"2.0\", \"method\": \"subtract\", \"params\": [42, 23], \"id\": 1}\n");
        writer.flush();
        JsonNode res = mapper.readTree(reader.readLine());
        assertEquals(res.path("result").path("value").asInt(), 19);
        socket.close();
        server.stop();
    }
    
}