package com.epitech.cashmanager.network

import java.io.IOException

/**
 * Server interface
 *
 * This interface is an instance of Socket
 *
 * @property Boolean isRunning allow to know if the server is launched
 */

interface SocketInterface {

    /**
     * Is the server launched
     * @return true if the server is running
     */

    val isRunning: Boolean

    /**
     * listen on the configured port
     * @throws IOException
     */

    @Throws(IOException::class)
    fun listen()

    /**
     * stop the listening
     */

    @Throws(IOException::class)
    fun stop()

}