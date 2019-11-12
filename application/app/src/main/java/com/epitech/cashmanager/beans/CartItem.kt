package com.epitech.cashmanager.beans

/**
 * Cart item
 *
 * This class represent an item of cart
 *
 * @property Product define the product
 * @property Int define the quantity of product
 */

data class CartItem(var product: Product, var quantity: Int = 0)