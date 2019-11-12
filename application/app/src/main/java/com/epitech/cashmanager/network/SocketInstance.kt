package com.epitech.cashmanager.network

import com.epitech.cashmanager.tools.Config
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.json.JSONObject
import java.net.*
import java.io.*

/**
 * Socket Instance
 */

class SocketInstance {
    private var clientSocket: Socket? = null
    private var out: PrintWriter? = null
    private var `in`: BufferedReader? = null

    private val config: Config = Config()
    private val mapper = ObjectMapper().registerModule(KotlinModule())

    fun start() {
        clientSocket = Socket(config.ip, config.port)
        out = PrintWriter(clientSocket!!.getOutputStream(), true)
        `in` = BufferedReader(InputStreamReader(clientSocket!!.getInputStream()))
    }

    /**
     * sendRCPFormatData
     *
     * This method is use for send a RCP format datas
     *
     * @param String method represent the name of method
     * @param JSONObject params represent the list of params datas
     * @param Int id represent unique identifier
     */

    fun sendRCPFormatData(method: String, params: JSONObject, id: Int) {
        var json: JSONObject = JSONObject()
        json.put("jsonrpc", "2.0")
        json.put("method", method)
        json.put("params", params)
        json.put("id", id)
        val serializedJSON = mapper.writeValueAsString(json)
        out!!.write(serializedJSON)
    }

    /**
     * getJSONRCPObect
     *
     * This method is use for deserialize string response to an JSONObject RCP format response
     */

    fun getJSONRCPObect(json: String): JSONObject {
        val deserializedJSON = mapper.readValue<JSONObject>(json)
        return deserializedJSON
    }

    fun stop() {
        `in`!!.close()
        out!!.close()
        clientSocket!!.close()
    }
}