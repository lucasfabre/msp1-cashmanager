package fr.cashmanager.rpc.clienthandler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.cashmanager.impl.helpers.JsonMapperFactory;
import fr.cashmanager.impl.ioc.ServicesContainer;
import fr.cashmanager.logging.Logger;
import fr.cashmanager.logging.LoggerFactory;
import fr.cashmanager.rpc.commands.IJsonRpcCommand;
import fr.cashmanager.rpc.commands.JsonRpcCommandManager;

/**
 * JsonRpcClientHandler
 */
public class JsonRpcClientHandler extends ClientHandler {

    private JsonRpcCommandManager commandManager;
    private Logger log;

    /**
     * default constructor
     * @param commandManager the command manager
     * @param socket the client socket
     */
    JsonRpcClientHandler(ServicesContainer services, JsonRpcCommandManager commandManager, Socket socket) {
        super(socket);
        this.log = services.get(LoggerFactory.class).getLogger("JsonRpcClientHandler");
        this.commandManager = commandManager;
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
        String commandAsString = "";
        do {
            try {
                commandAsString = reader.readLine();
                if (commandAsString == null) {
                    continue;
                }
                JsonNode command = mapper.readTree(commandAsString);
                JsonNode commandResult = parseAndExecuteCommand(command);
                JsonNode clientResult = formatClientResult(getId(command), commandResult);
                String clientResultAsString = mapper.writeValueAsString(clientResult);
                writer.write(clientResultAsString);
                writer.write("\n");
                writer.flush();
            } catch (Exception e) {
                log.error("Exception while processing command", e);
                JsonNode clientErrorJsonNode = formatClientError(e);
                writer.write(mapper.writeValueAsString(clientErrorJsonNode) + "\n");
                writer.flush();
            }
        } while(commandAsString != null);
        log.info("Client disconected");
    }

    /**
     * private implementation where we parse and execute a command
     * @param tree the parsed json object
     * @return the result jsonNode
     * @throws Exception if an exception occur durring the process
     */
    private JsonNode parseAndExecuteCommand(JsonNode tree) throws Exception {
        ObjectMapper mapper = JsonMapperFactory.getObjectMapper();
        String method = getMethod(tree);
        if (method == null) {
            throw new Exception("no method for command [" + mapper.writeValueAsString(tree) + "]");
        }
        IJsonRpcCommand command = commandManager.getCommandForMethod(method).newInstance();
        if (command == null) {
            throw new Exception("no command for method [" + method + "]");
        }
        JsonNode params = getParams(tree);
        if (params == null) {
            throw new Exception("no params in command [" + mapper.writeValueAsString(tree) + "]");
        }
        command.parseParams(params); // throws an exception in case of an error
        return command.execute(); // throws an exception in case of an error
    }

    /**
     * format the result to a JSON-RPC result
     * @param id the id of the command
     * @param commandResult the result of the command
     * @return the formated JSON Object
     */
    private JsonNode formatClientResult(int id, JsonNode commandResult) {
        ObjectMapper mapper = JsonMapperFactory.getObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        result.put("jsonrpc", "2.0");
        result.put("id", id);
        result.set("result", commandResult);
        return result;
    }

    /**
     * format the error to a JSON-RPC error
     * @param e the exception
     * @return the formated JSON Object
     */
    private JsonNode formatClientError(Exception e) {
        ObjectMapper mapper = JsonMapperFactory.getObjectMapper();
        ObjectNode errorNode = mapper.createObjectNode();
        errorNode.put("code", -42);
        errorNode.put("message", e.getMessage());
        ObjectNode result = mapper.createObjectNode();
        result.put("jsonrpc", "2.0");
        result.set("id", mapper.nullNode());
        result.set("error", errorNode);
        return result;
    }

    /**
     * helper to get the method in the JSON-RPC request
     * @param command
     * @return the the method string
     */
    private String getMethod(JsonNode command) {
        return command.path("method").asText();
    }

    /**
     * helper to get the params in the JSON-RPC request
     * @param command
     * @return the the params jsonNode
     */
    private JsonNode getParams(JsonNode command) {
        return command.get("params");
    }

    /**
     * helper to get the id in the JSON-RPC request
     * @param command
     * @return the the id
     */
    private int getId(JsonNode command) {
        return command.path("id").asInt();
    }
}
