package com.epitech.cashmanager.services

import android.content.Context
import android.widget.Toast
import com.epitech.cashmanager.beans.CartItem
import com.epitech.cashmanager.repositories.ShoppingCartRepository

/**
 * Socket Instance
 *
 * This class is an instance of Socket
 *
 * @property ShoppingCartRepository cartRepository represent cart data persistance
 */

class ShoppingCartService {

    companion object {

        private val cartRepository = ShoppingCartRepository()

        /**
         * addItem
         *
         * This method is use for add an cartItem to cart
         *
         * @param CartItem cartItem represent item datas of cart
         */

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

        /**
         * removeItem
         *
         * This method is use for remove an cartItem to cart
         *
         * @param CartItem cartItem represent item datas of cart
         * @param Context context represent itemView context
         */

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

        /**
         * saveCart
         *
         * This method is use for save list of CartItem in cart
         *
         * @param MutableList<CartItem> cart represent a mutable list of CartItem
         */

        fun saveCart(cart: MutableList<CartItem>) {
            cartRepository.saveCart(cart)
        }

        /**
         * getCart
         *
         * This method is use for get list of CartItem in cart
         *
         * @return MutableList<CartItem>
         */

        fun getCart(): MutableList<CartItem> {
            return cartRepository.getCart()
        }

        /**
         * getShoppingCartSize
         *
         * This method is use for get size of cart (quantity of products)
         *
         * @return Int
         */

        fun getShoppingCartSize(): Int {
            var cartSize = 0
            getCart().forEach {
                cartSize += it.quantity;
            }
            return cartSize
        }
    }

}