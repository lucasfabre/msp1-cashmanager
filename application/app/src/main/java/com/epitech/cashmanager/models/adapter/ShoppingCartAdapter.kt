package com.epitech.cashmanager.models.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.epitech.cashmanager.R
import com.epitech.cashmanager.beans.CartItem
import kotlinx.android.synthetic.main.cart_list_item.view.*

class ShoppingCartAdapter(var context: Context, var cartItems: List<CartItem>) :
    RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.cart_list_item, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindItem(cartItems[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bindItem(cartItem: CartItem) {
            if(cartItem.quantity > 0) {
                val nonNullableInt: Int = cartItem.product.photos!!
                itemView.product_image.setImageResource(nonNullableInt)
                itemView.product_name.text = cartItem.product.name
                itemView.product_price.text = "$${cartItem.product.price}"
                itemView.product_quantity.text = cartItem.quantity.toString()
            }
        }
    }


}