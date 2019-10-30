package com.epitech.cashmanager.services

import android.content.Context
import android.widget.Toast
import com.epitech.cashmanager.beans.CartItem
import com.epitech.cashmanager.repositories.ShoppingCartRepository

class ShoppingCartService {

    companion object {

        private val cartRepository = ShoppingCartRepository()

        fun addItem(cartItem: CartItem) {
            val cart = getCart()

            val targetItem = cart.singleOrNull { it.product.id == cartItem.product.id }

            if (targetItem == null) {
                cartItem.quantity++
                cart.add(cartItem)
            } else {

                targetItem.quantity++
            }
            saveCart(cart)

        }

        fun removeItem(cartItem: CartItem, context: Context) {

            val cart = getCart()


            val targetItem = cart.singleOrNull { it.product.id == cartItem.product.id }

            if (targetItem != null) {

                if (targetItem.quantity > 0) {

                    Toast.makeText(context, "great quantity", Toast.LENGTH_SHORT).show()
                    targetItem.quantity--
                } else {
                    cart.remove(targetItem)
                }

            }

            saveCart(cart)

        }

        fun saveCart(cart: MutableList<CartItem>) {
            cartRepository.saveCart(cart)
        }

        fun getCart(): MutableList<CartItem> {
            return cartRepository.getCart()
        }

        fun getShoppingCartSize(): Int {
            var cartSize = 0
            getCart().forEach {
                cartSize += it.quantity;
            }
            return cartSize
        }
    }

}