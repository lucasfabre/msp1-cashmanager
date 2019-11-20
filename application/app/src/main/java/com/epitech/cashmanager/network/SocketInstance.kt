package com.epitech.cashmanager.network

import com.epitech.cashmanager.exceptions.ResponseRCPException
import com.epitech.cashmanager.tools.Config
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.net.*
import java.io.*
import java.lang.Exception

/**
 * Socket Instance
 *
 * This class is an instance of Socket
 *
 * @property ServerSocket the serverSocket define the server
 * @property Socket the clientSocket define the client
 * @property PrintWriter out define output
 * @property BufferedReader 'in' define input
 */

class SocketInstance {
    private var clientSocket: Socket? = null
    private var out: PrintWriter? = null
    private var `in`: BufferedReader? = null

    private val config: Config = Config()
    private val mapper = ObjectMapper().registerModule(KotlinModule())

    /**
     * start
     *
     * This method is use for start a Socket
     *
     * @param Int port represent the port of the app
     */

    fun start() {
        clientSocket = Socket(config.ip, config.port)
        out = PrintWriter(clientSocket!!.getOutputStream(), true)
        `in` = BufferedReader(InputStreamReader(clientSocket!!.getInputStream()))
    }

    /**
     * sendRCPFormatData
     *
     * This method is use for serialize and send a RCP format datas
     *
     * @param String method represent the name of method
     * @param JSONObject params represent the list of params datas
     * @param Int id represent unique identifier
     */

    fun sendRCPFormatData(method: String, params: ObjectNode, id: Int) {
        var json: ObjectNode = mapper.createObjectNode()
        json.put("jsonrpc", "2.0")
        json.put("method", method)
        json.put("params", params)
        json.put("id", id)
        val serializedJSON = mapper.writeValueAsString(json)
        out!!.write(serializedJSON + "\n")
        out!!.flush()
    }

    /**
     * getJSONRCPObect
     *
     * This method is use for deserialize string response to an JSONObject RCP format response
     */

    fun getJsonRcpObject(): ObjectNode {
        val deserializedJSON: ObjectNode = mapper.createObjectNode()
        try {
            var line: String? = `in`!!.readLine()
            if (line == null) {
                throw Exception()
            }
            val deserializedJSON = mapper.readValue<ObjectNode>(line)
            deserializedJSON.get("method")
            if (deserializedJSON.has("error")) {
                var code: String = deserializedJSON.get("error").asText("code")
                var message: String = deserializedJSON.get("error").asText("message")
                throw ResponseRCPException("Code: " + code + ", Message: " + message)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return deserializedJSON
    }

    /**
     * stop
     *
     * This method is use for stop a Socket
     */

    fun stop() {
        `in`!!.close()
        out!!.close()
        clientSocket!!.close()
    }

    fun isConnected(): Boolean {
        return clientSocket!!.isConnected()
    }
}