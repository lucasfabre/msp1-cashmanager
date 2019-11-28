package com.epitech.cashmanager

import android.os.Looper
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.epitech.cashmanager.beans.CartItem
import com.epitech.cashmanager.beans.Product
import com.epitech.cashmanager.services.ShoppingCartService
import com.epitech.cashmanager.services.ShoppingCartService.Companion.getCart
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import io.paperdb.Paper

import org.junit.Test
import org.junit.runner.RunWith

/**
 * Shopping Cart Test
 *
 * This class allow to test shopping cart
 */

@RunWith(AndroidJUnit4::class)
class ShoppingCartTest {

    val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun addItem() {
        val product = Product("Test", 1, "name", "200", "2010398476")
        var cartItem: CartItem = CartItem(product, 2)
        Paper.init(appContext)
        ShoppingCartService.addItem(cartItem)
        assertNotNull(ShoppingCartService.getCart())
    }

    @Test
    fun removeItem() {
        val cart = getCart()
        val cartItem = cart[0]
        Looper.prepare()
        val quantityBefore = ShoppingCartService.getTargetItem(cartItem, cart)!!.quantity
        ShoppingCartService.removeItem(cartItem, appContext)
        val quantityAfter = ShoppingCartService.getTargetItem(cartItem, cart)!!.quantity
        assertTrue(quantityAfter < quantityBefore)
    }

    @Test
    fun saveCart() {
        addItem()
        val cart = getCart()
        ShoppingCartService.saveCart(cart)
        assertNotNull(ShoppingCartService.getCart())
    }

}
