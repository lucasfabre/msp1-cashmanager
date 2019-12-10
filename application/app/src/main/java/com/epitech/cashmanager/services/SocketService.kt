@file:Suppress("DEPRECATION", "NAME_SHADOWING")
package com.epitech.cashmanager.services

import android.util.Log
import com.epitech.cashmanager.exceptions.ResponseRCPException
import com.epitech.cashmanager.network.SocketInstance
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.lang.Exception

/**
 * Socket service
 *
 * This class is use for define JSON-RCP communication with a server
 */

class SocketService {

    private val mapper = ObjectMapper().registerModule(KotlinModule())
    private val socket = SocketInstance()

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
        println("###### Out ######: " + serializedJSON)
        socket.out!!.write(serializedJSON + "\n")
        socket.out!!.flush()
    }

    /**
     * getJSONRCPObect
     *
     * This method is use for deserialize string response to an JSONObject RCP format response
     */

    fun getJsonRcpObject(): ObjectNode {
        val deserializedJSON: ObjectNode = mapper.createObjectNode()
        try {
            var line: String? = socket.`in`!!.readLine()
            println("###### In ######: " + line)
            if (line == null) {
                throw Exception()
            }
            val deserializedJSON = mapper.readValue<ObjectNode>(line)
            deserializedJSON.get("method")
            if (deserializedJSON.has("error")) {
                var code: String = deserializedJSON.get("error").get("code").asText()
                var message: String = deserializedJSON.get("error").get("message").asText()
                throw ResponseRCPException("Code: " + code + ", Message: " + message)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return deserializedJSON
    }

    /**
     * isConnected
     *
     * This method is check status of connexion
     */

    fun isConnected(): Boolean {
        return socket.clientSocket!!.isConnected()
    }

    /**
     * getSocket
     *
     * This method is use for return current socketInstance
     */

    fun getSocket(): SocketInstance {
        return socket
    }

}