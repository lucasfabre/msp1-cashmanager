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
import com.epitech.cashmanager.services.ShoppingCartService
import com.epitech.cashmanager.beans.CartItem
import com.epitech.cashmanager.beans.Product
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.adapter_product_row_item.view.*

/**
 * Product adapter
 *
 * This class permit of manage product
 *
 * @return return an RecyclerView.Adapter<ProductAdapter.ViewHolder>
 */

class ProductAdapter(var context: Context, var products: List<Product> = arrayListOf()):
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_product_row_item, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindProduct(products[position])
        (context as MainActivity).coordinator
    }

    /**
     * ViewHolder
     *
     * This class define a part of global view
     *
     * @param View define the view
     */

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("CheckResult", "SetTextI18n")
        fun bindProduct(product: Product) {
            //val nonNullableInt: Int = product.photos!!
            itemView.product_name.text = product.name
            itemView.product_price.text = "$${product.price.toString()}"
            Picasso.get().load(product.photos).into(itemView.product_image)
            //itemView.product_image.setImageBitmap(product.photos)
            Observable.create(ObservableOnSubscribe<MutableList<CartItem>> {
                itemView.addToCart.setOnClickListener { _ ->
                    val item = CartItem(product)
                    ShoppingCartService.addItem(item)
                    //notify users
                    Snackbar.make(
                        (itemView.context as MainActivity).coordinator,
                        "${product.name} added to cart",
                        Snackbar.LENGTH_LONG
                    ).show()
                    it.onNext(ShoppingCartService.getCart())
                }
                itemView.removeItem.setOnClickListener { _ ->
                    val item = CartItem(product)
                    ShoppingCartService.removeItem(item, itemView.context)
                    Snackbar.make(
                        (itemView.context as MainActivity).coordinator,
                        "${product.name} deleted to cart",
                        Snackbar.LENGTH_LONG
                    ).show()
                    it.onNext(ShoppingCartService.getCart())
                }
            }).subscribe { cart ->
                var quantity = 0
                cart.forEach { cartItem ->
                    quantity += cartItem.quantity
                }
                (itemView.context as MainActivity).cart_size.text = quantity.toString()
                Toast.makeText(itemView.context, "size of cart $quantity", Toast.LENGTH_SHORT).show()
            }
        }
    }
}