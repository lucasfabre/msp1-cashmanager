package com.epitech.cashmanager.network

import java.net.*
import java.io.*

/**
 * Socket Instance
 */

class SocketInstance {
    private var serverSocket: ServerSocket? = null
    private var clientSocket: Socket? = null
    private var out: PrintWriter? = null
    private var `in`: BufferedReader? = null

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