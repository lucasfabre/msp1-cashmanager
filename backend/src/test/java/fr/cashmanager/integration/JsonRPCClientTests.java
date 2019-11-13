package fr.cashmanager.integration;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;

import fr.cashmanager.config.IConfig;
import fr.cashmanager.config.Preference;
import fr.cashmanager.impl.helpers.JsonMapperFactory;
import fr.cashmanager.logging.LoggerFactory;
import fr.cashmanager.logging.LoggerService;
import fr.cashmanager.rpc.clienthandler.ClientHandlerFactory;
import fr.cashmanager.rpc.clienthandler.JsonRpcClientHandlerFactory;
import fr.cashmanager.rpc.commands.IJsonRpcCommand;
import fr.cashmanager.rpc.commands.JsonRpcCommandManager;
import fr.cashmanager.rpc.exception.JsonRpcException;
import fr.cashmanager.rpc.exception.JsonRpcErrorCode;
import fr.cashmanager.rpc.middlewares.CommandMiddleware;
import fr.cashmanager.rpc.middlewares.ErrorMiddleware;
import fr.cashmanager.rpc.server.IServer;
import fr.cashmanager.rpc.server.SocketServer;

/**
 * SocketServerTests
 */
public class JsonRPCClientTests extends IntegrationTestBase {

    protected int SERVER_PORT = 3812;

    class SubstractCommand implements IJsonRpcCommand {
        private List<Integer> params = null;
        @Override
        public void parseParams(JsonNode params) throws JsonRpcException {
            ObjectMapper mapper = JsonMapperFactory.getObjectMapper();
            JavaType jt = mapper.getTypeFactory().constructType(new TypeReference<List<Integer>>() {
            });
            try {
                this.params = mapper.readValue(mapper.treeAsTokens(params), jt);
            } catch (IOException e) {
                throw new JsonRpcException(JsonRpcErrorCode.INVALID_PARAMS);
			}
        }

        @Override
        public String getMethodName() {
            return "subtract";
        }

        @Override
        public JsonNode execute() throws JsonRpcException {
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

    @Before
    public void setUpAppContext() throws Exception {
        // IConfig
        IConfig config = new IConfig() {
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
        services.register(IConfig.class, config);
        // Logging
        LoggerService loggerService = new LoggerService(services);
        services.register(LoggerService.class, loggerService);
        LoggerFactory loggerFactory = new LoggerFactory(services);
        services.register(LoggerFactory.class, loggerFactory);
        // JsonRpcCommandManager
        JsonRpcCommandManager jsonRpcCommandManager = new JsonRpcCommandManager();
        jsonRpcCommandManager.registerCommand(new SubstractCommand());
        jsonRpcCommandManager.registerMiddleware(new ErrorMiddleware());
        jsonRpcCommandManager.registerMiddleware(new CommandMiddleware(services));
        services.register(JsonRpcCommandManager.class, jsonRpcCommandManager);
        // ClientHandlerFactory
        ClientHandlerFactory clientHandlerFactory = new JsonRpcClientHandlerFactory(services);
        services.register(ClientHandlerFactory.class, clientHandlerFactory);
        // IServer
        SocketServer server = new SocketServer(services);
        services.register(IServer.class, server);
        // init
        config.configure();
    }

    @Test
    public void testSocket() throws Exception {
        ObjectMapper mapper = JsonMapperFactory.getObjectMapper();
        waitForServerStarted();
        // Client
        initClientConection(SERVER_PORT);
        String response = writeMessageAndWaitResponse("{\"jsonrpc\": \"2.0\", \"method\": \"subtract\", \"params\": [42, 23], \"id\": 1}");
        JsonNode res = mapper.readTree(response);
        assertEquals(res.path("result").path("value").asInt(), 19);
        closeConectionAndStopServer();
    }
    
}