package com.epitech.cashmanager.network

import com.epitech.cashmanager.tools.Config
import java.net.*
import java.io.*

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
    var clientSocket: Socket? = null
    var out: PrintWriter? = null
    var `in`: BufferedReader? = null

    private val config: Config = Config()

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
     * stop
     *
     * This method is use for stop a Socket
     */

    fun stop() {
        `in`!!.close()
        out!!.close()
        clientSocket!!.close()
    }
}