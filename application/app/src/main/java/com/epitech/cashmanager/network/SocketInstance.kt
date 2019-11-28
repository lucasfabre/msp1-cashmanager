package com.epitech.cashmanager.network

import android.content.Context
import android.view.Gravity
import com.epitech.cashmanager.tools.Config
import com.sdsmdg.tastytoast.TastyToast
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

    fun start(hostname: String, context: Context) {
        try {
            clientSocket = Socket(hostname, config.port)
            out = PrintWriter(clientSocket!!.getOutputStream(), true)
            `in` = BufferedReader(InputStreamReader(clientSocket!!.getInputStream()))
            TastyToast.makeText(
                context,
                "Connected",
                TastyToast.LENGTH_SHORT,
                TastyToast.SUCCESS
            ).setGravity(Gravity.BOTTOM, 0, 150)
        } catch (e: Exception) {
            TastyToast.makeText(
                context,
                e.message,
                TastyToast.LENGTH_LONG,
                TastyToast.ERROR
            ).setGravity(Gravity.BOTTOM, 0, 150)
            e.printStackTrace()
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
    }
}