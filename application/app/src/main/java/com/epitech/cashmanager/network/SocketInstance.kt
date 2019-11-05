package com.epitech.cashmanager.network

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

    private var serverSocket: ServerSocket? = null
    private var clientSocket: Socket? = null
    private var out: PrintWriter? = null
    private var `in`: BufferedReader? = null

    /**
     * start
     *
     * This method is use for start a Socket
     *
     * @param Int port represent the port of the app
     */

    fun start(port: Int) {
        serverSocket = ServerSocket(port)
        clientSocket = serverSocket!!.accept()
        out = PrintWriter(clientSocket!!.getOutputStream(), true)
        `in` = BufferedReader(InputStreamReader(clientSocket!!.getInputStream()))
        val greeting = `in`!!.readLine()
        if ("hello server" == greeting) {
            out!!.println("hello client")
        } else {
            out!!.println("unrecognised greeting")
        }
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
        serverSocket!!.close()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val socket = SocketInstance()
            socket.start(6666)
        }
    }

}