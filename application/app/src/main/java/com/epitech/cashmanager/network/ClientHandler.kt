package com.epitech.cashmanager.network

import java.net.*
import java.io.*

/**
 * Client handler
 *
 * This class is an instance of client
 *
 * @property Socket clientSocket define an socket communication
 * @property PrintWriter out define format representation for a text-output stream
 * @property BufferedReader `in` define format representation for a character-input stream
 */

class ClientHandler {

    private var clientSocket: Socket? = null
    private var out: PrintWriter? = null
    private var `in`: BufferedReader? = null

    /**
     * startConnection
     *
     * This method is use for start the socket connection
     *
     * @param String ip represent IP adress of the server for the connexion
     * @param Int port represent port of the server for the connexion
     */

    fun startConnection(ip: String, port: Int) {
        clientSocket = Socket(ip, port)
        out = PrintWriter(clientSocket!!.getOutputStream(), true)
        `in` = BufferedReader(InputStreamReader(clientSocket!!.getInputStream()))
    }

    /**
     * sendMessage
     *
     * This method is use for send a message by socket
     *
     * @param String msg represent the sending of message
     */

    fun sendMessage(msg: String): String {
        out!!.println(msg)
        return `in`!!.readLine()
    }

    /**
     * stopConnection
     *
     * This method is use for stop a socket connection
     */

    fun stopConnection() {
        `in`!!.close()
        out!!.close()
        clientSocket!!.close()
    }

}