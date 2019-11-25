package com.epitech.cashmanager.repositories

import com.epitech.cashmanager.beans.CartItem
import io.paperdb.Paper

class ShoppingCartRepository {

    fun saveCart(cart: MutableList<CartItem>) {
        Paper.book().write("cart", cart)
    }

    fun getCart(): MutableList<CartItem> {
        return Paper.book().read("cart", mutableListOf())
    }

    fun clearCart(){
        Paper.book().destroy()
    }
}