package com.epitech.cashmanager.models.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.epitech.cashmanager.R
import com.epitech.cashmanager.beans.CartItem
import com.epitech.cashmanager.services.ShoppingCartService
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import kotlinx.android.synthetic.main.adapter_cart_list_item.view.*
import kotlinx.android.synthetic.main.adapter_cart_list_item.view.product_image
import kotlinx.android.synthetic.main.adapter_cart_list_item.view.product_name
import kotlinx.android.synthetic.main.adapter_cart_list_item.view.product_price
import kotlinx.android.synthetic.main.adapter_cart_list_item.view.removeProduct
import kotlinx.android.synthetic.main.adapter_cart_list_item.view.addProduct

/**
 * Shopping cart adapter
 *
 * This class is an instance of Socket
 *
 * @return RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder>
 */

class ShoppingCartAdapter(var context: Context, var cartItems: List<CartItem>) :
    RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.adapter_cart_list_item, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindItem(cartItems[position])
    }

    /**
     * ViewHolder
     *
     * This class define a part of global view
     *
     * @param View define the view
     */

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n", "CheckResult")
        fun bindItem(cartItem: CartItem) {
            if(cartItem.quantity > 0) {
                //itemView.product_image.setImageBitmap(cartItem.product.photos)
                Picasso.get().load(cartItem.product.photos).into(itemView.product_image)
                itemView.product_name.text = cartItem.product.name
                itemView.product_price.text = "$${cartItem.product.price}"
                itemView.product_quantity.text = cartItem.quantity.toString()
                Observable.create(ObservableOnSubscribe<MutableList<CartItem>> {
                    itemView.addProduct.setOnClickListener { _ ->
                        ShoppingCartService.addItem(cartItem)
                        it.onNext(ShoppingCartService.getCart())

                    }
                    itemView.removeProduct.setOnClickListener { _ ->
                        ShoppingCartService.removeItem(cartItem, itemView.context)
                        it.onNext(ShoppingCartService.getCart())
                    }
                }).subscribe { cart ->
                    var quantity = 0
                    cart.forEach { cartItem ->
                        quantity += cartItem.quantity
                    }
                }
            }
        }
    }
}