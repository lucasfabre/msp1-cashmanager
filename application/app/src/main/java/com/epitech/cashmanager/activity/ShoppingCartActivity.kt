package com.epitech.cashmanager.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.epitech.cashmanager.R
import com.epitech.cashmanager.models.adapter.ShoppingCartAdapter
import com.epitech.cashmanager.services.ShoppingCartService
import kotlinx.android.synthetic.main.activity_shopping_cart.*
import kotlinx.android.synthetic.main.fragment_shopping_cart.*

class ShoppingCartActivity : AppCompatActivity() {

    lateinit var adapter: ShoppingCartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_cart)
        title = "Shopping Cart"
        adapter = ShoppingCartAdapter(this, ShoppingCartService.getCart())
        adapter.notifyDataSetChanged()

        shopping_cart_recyclerView.adapter = adapter
        shopping_cart_recyclerView.layoutManager = LinearLayoutManager(this)

        val totalPrice = ShoppingCartService.getCart()
            .fold(0.toDouble()) { acc, cartItem -> acc + cartItem.quantity.times(cartItem.product.price!!.toDouble()) }
        total_price.text = "${totalPrice}€"
        paiementButton.setOnClickListener{
            val intent = Intent(this, PaiementActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
