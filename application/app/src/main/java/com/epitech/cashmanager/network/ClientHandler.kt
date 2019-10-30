package com.epitech.cashmanager.network

import java.net.*
import java.io.*

/**
 * Client Handler
 */

class ClientHandler {
    private var clientSocket: Socket? = null
    private var out: PrintWriter? = null
    private var `in`: BufferedReader? = null

    fun startConnection(ip: String, port: Int) {
        clientSocket = Socket(ip, port)
        out = PrintWriter(clientSocket!!.getOutputStream(), true)
        `in` = BufferedReader(InputStreamReader(clientSocket!!.getInputStream()))
    }

    fun sendMessage(msg: String): String {
        out!!.println(msg)
        return `in`!!.readLine()
    }

    fun stopConnection() {
        `in`!!.close()
        out!!.close()
        clientSocket!!.close()
}
}