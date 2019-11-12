package fr.cashmanager.rpc;

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

/**
 * JsonRpcClientHandler
 */
public class JsonRpcClientHandler extends ClientHandler {

    private ObjectMapper mapper = new ObjectMapper();
    private JsonRpcCommandManager commandManager;

    /**
     * default constructor
     * @param commandManager the command manager
     * @param socket the client socket
     */
    JsonRpcClientHandler(JsonRpcCommandManager commandManager, Socket socket) {
        super(socket);
        this.commandManager = commandManager;
    }

    /**
     * the main function where we handle the client stream
     * @param is the socket input stream
     * @param os the socket output stream
     */
    @Override
    public void handleClient(InputStream is, OutputStream os) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8);
        do {
            try {
                String commandAsString = reader.readLine();
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
            } catch (SocketException e) {
                // client disconected so do not write the error to the client
            } catch (Exception e) {
                e.printStackTrace();
                JsonNode clientErrorJsonNode = formatClientError(e);
                writer.write(mapper.writeValueAsString(clientErrorJsonNode) + "\n");
                writer.flush();
            }
        } while(this.getSocket().isConnected() == true);
    }

    /**
     * private implementation where we parse and execute a command
     * @param tree the parsed json object
     * @return the result jsonNode
     * @throws Exception if an exception occur durring the process
     */
    private JsonNode parseAndExecuteCommand(JsonNode tree) throws Exception {
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
