package com.epitech.cashmanager.services

import android.content.Context
import android.os.Looper
import android.widget.Toast
import com.epitech.cashmanager.R
import com.epitech.cashmanager.beans.CartItem
import com.epitech.cashmanager.beans.Product
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
            val targetItem = getTargetItem(cartItem, cart)
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
            val targetItem = getTargetItem(cartItem, cart)
            if (targetItem != null) {
                if (targetItem.quantity > 1) {
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
         * getTargetItem
         *
         * This method is use for get the targetItem of CartItem in cart
         *
         * @return CartItem
         */

        fun getTargetItem(cartItem: CartItem, cart: MutableList<CartItem>): CartItem? {
            return cart.singleOrNull { it.product.id == cartItem.product.id }
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

        /**
         * initProducts
         *
         * This method is use for initialise product
         *
         * @return ArrayList<Product>
         */

        fun initProducts(): ArrayList<Product> {
            var products = ArrayList<Product>()
            val product = Product("basic description", 1, "Huawei P30", "460", "https://cdn.shopify.com/s/files/1/0158/2612/4864/products/1_608x608.jpg?v=1566905322")
            val product2 = Product("basic description", 2, "Samsung S9", "370","https://images.frandroid.com/wp-content/uploads/2019/04/samsung-galaxy-s9-plus-android.png" )
            val product3 = Product("basic description", 3, "Samsung S10", "600", "https://csmobiles.com/15710-large_default/samsung-galaxy-s10-g973f-128go-dual-sim-bleu.jpg")
            val product4 = Product("basic description", 4, "Iphone 11 Pro", "1100", "https://static.fnac-static.com/multimedia/Images/FR/MDM/7a/b2/bd/12431994/1505-1/tsp20191113133346/Apple-iPhone-11-Pro-64-Go-5-8-Gris-Sideral.jpg")
            val product5 = Product("basic description", 5, "Surface Pro X", "1549", "https://img-prod-cms-rt-microsoft-com.akamaized.net/cms/api/am/imageFileData/RE3onLg?ver=3fa8&q=90&m=6&h=705&w=1253&b=%23FFF0F0F0&f=jpg&o=f&p=140&aim=true")
            val product6 = Product("basic description", 6, "MacBook Pro", "3100", "https://images.fr.shopping.rakuten.com/photo/1141349434.jpg")
            products.add(product)
            products.add(product2)
            products.add(product3)
            products.add(product4)
            products.add(product5)
            products.add(product6)
            return products
        }
        fun clearCart(){
            cartRepository.clearCart()
        }
    }
}