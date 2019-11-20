package fr.cashmanager.rpc.clienthandler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.cashmanager.impl.helpers.JsonMapperFactory;
import fr.cashmanager.impl.ioc.ServicesContainer;
import fr.cashmanager.logging.Logger;
import fr.cashmanager.logging.LoggerFactory;
import fr.cashmanager.rpc.commands.JsonRpcCommandManager;
import fr.cashmanager.rpc.exception.JsonRpcException;
import fr.cashmanager.rpc.exception.JsonRpcErrorCode;
import fr.cashmanager.rpc.helpers.JsonRpcHelper;
import fr.cashmanager.rpc.middlewares.JsonRpcMiddleware;

/**
 * JsonRpcClientHandler
 */
public class JsonRpcClientHandler extends ClientHandler {

    private ServicesContainer services;
    private Logger log;

    /**
     * default constructor
     * @param commandManager the command manager
     * @param socket the client socket
     */
    JsonRpcClientHandler(ServicesContainer services, Socket socket) {
        super(socket);
        this.services = services;
        this.log = services.get(LoggerFactory.class).getLogger("JsonRpcClientHandler");
    }

    /**
     * the main function where we handle the client stream
     * @param is the socket input stream
     * @param os the socket output stream
     */
    @Override
    public void handleClient(InputStream is, OutputStream os) throws Exception {
        log.info("Client connected");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8);
        ObjectMapper mapper = JsonMapperFactory.getObjectMapper();
        Map<String, Object> sessionData = new HashMap<String, Object>();
        String bodyAsString = "";
        do {
            bodyAsString = reader.readLine();
            if (bodyAsString == null) {
                continue;
            }
            JsonNode commandResult;
            try {
                JsonNode body = mapper.readTree(bodyAsString);
                commandResult = executeMiddlewareChain(sessionData, body);
            } catch (JsonParseException e) {
                commandResult = JsonRpcHelper.formatClientError(null,
                    new JsonRpcException(JsonRpcErrorCode.INVALID_REQUEST));
            }
            String commandResultAsString = mapper.writeValueAsString(commandResult);
            writer.write(commandResultAsString);
            writer.write("\n");
            writer.flush();
        } while(bodyAsString != null);
        log.info("Client disconected");
    }

    private JsonNode executeMiddlewareChain(Map<String, Object> sessionData, JsonNode body) {
        Queue<JsonRpcMiddleware> chain = services.get(JsonRpcCommandManager.class).getMiddlewareQueue();
        JsonRpcMiddleware first = chain.poll();
        try {
            return first.run(chain, sessionData, body);
        } catch (JsonRpcException e) {
            String message = "Strange a JsonRpcException was returned by the command chain. did you forget the ErrorMiddleware ?";
            log.error(message, e);
            throw new RuntimeException(message);
        }
    }

}
