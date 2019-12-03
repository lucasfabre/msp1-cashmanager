@file:Suppress("DEPRECATION")

package com.epitech.cashmanager

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.epitech.cashmanager.network.SocketInstance
import com.epitech.cashmanager.services.SocketService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.KotlinModule
import junit.framework.Assert.*

import org.junit.Test
import org.junit.runner.RunWith

/**
 * Shopping Cart Test
 *
 * This class allow to test shopping cart
 */

@RunWith(AndroidJUnit4::class)
class SocketTest {

    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    private val socketInstance: SocketInstance = SocketInstance()
    private val socketService: SocketService = SocketService()
    private val mapper = ObjectMapper().registerModule(KotlinModule())

    @Test
    fun start() {
        socketInstance.start("10.0.0.2", appContext)
        assertNotNull(socketInstance.out)
        assertNotNull(socketInstance.`in`)
        socketInstance.stop()
    }

    @Test
    fun sendRCPFormatData() {
        var params: ObjectNode = mapper.createObjectNode()
        params.put("accountId", "acc1")
        socketService.getSocket().start("10.0.0.2", appContext)
        socketService.sendRCPFormatData("DescribeAccount", params, 1)
        assertNotNull(socketService.getJsonRcpObject())
        socketInstance.stop()
    }

    @Test
    fun getJsonRcpObject() {
        val objectNode = socketService.getJsonRcpObject()
        assertFalse(objectNode.asText().isEmpty())
    }

    @Test
    fun stop() {
        socketInstance.stop()
        assertNull(socketInstance.out)
        assertNull(socketInstance.`in`)
        assertNull(socketInstance.clientSocket)
    }

}
