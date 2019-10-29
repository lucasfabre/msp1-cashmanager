package com.epitech.cashmanager.models.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.epitech.cashmanager.activity.MainActivity
import com.epitech.cashmanager.R
import com.epitech.cashmanager.services.ShoppingCart
import com.epitech.cashmanager.beans.CartItem
import com.epitech.cashmanager.beans.Product
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.product_row_item.view.*

class ProductAdapter(var context: Context, var products: List<Product> = arrayListOf()):
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.product_row_item, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindProduct(products[position])
        (context as MainActivity).coordinator
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("CheckResult", "SetTextI18n")
        fun bindProduct(product: Product) {
            val nonNullableInt: Int = product.photos!!
            itemView.product_name.text = product.name
            itemView.product_price.text = "$${product.price.toString()}"
            if (nonNullableInt > 1) {
                itemView.product_image.setImageResource(nonNullableInt)
            }

            Observable.create(ObservableOnSubscribe<MutableList<CartItem>> {
                itemView.addToCart.setOnClickListener { view ->

                    val item = CartItem(product)

                    ShoppingCart.addItem(item)
                    //notify users
                    Snackbar.make(
                        (itemView.context as MainActivity).coordinator,
                        "${product.name} ajouté au panier",
                        Snackbar.LENGTH_LONG
                    ).show()
                    it.onNext(ShoppingCart.getCart())
                }
                itemView.removeItem.setOnClickListener { view ->
                    val item = CartItem(product)
                    ShoppingCart.removeItem(item, itemView.context)
                    Snackbar.make(
                        (itemView.context as MainActivity).coordinator,
                        "${product.name} enlever du panier",
                        Snackbar.LENGTH_LONG
                    ).show()
                    it.onNext(ShoppingCart.getCart())
                }
            }).subscribe { cart ->
                var quantity = 0
                cart.forEach { cartItem ->
                    quantity += cartItem.quantity
                }
                (itemView.context as MainActivity).cart_size.text = quantity.toString()
                Toast.makeText(itemView.context, "taille du panier $quantity", Toast.LENGTH_SHORT).show()
            }
        }
    }
}