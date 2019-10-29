package com.epitech.cashmanager.network

import java.net.Socket

/**
 * Client Handler Factory
 */

interface ClientHandlerFactory {

    /**
     * create a clientHandler
     * @return a new ClientHandler
     */
    fun create(socket: Socket): ClientHandler

}