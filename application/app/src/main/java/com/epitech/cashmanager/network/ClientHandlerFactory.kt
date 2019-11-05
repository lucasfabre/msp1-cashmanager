package com.epitech.cashmanager.network

import java.net.Socket

/**
 * Client handler factory
 *
 * This interface define a client factory
 */

interface ClientHandlerFactory {

    /**
     * clientHandler
     *
     * This method permit to create clientHandler
     * @return a new ClientHandler
     */

    fun create(socket: Socket): ClientHandler

}