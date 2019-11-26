package com.epitech.cashmanager.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.epitech.cashmanager.R
import com.epitech.cashmanager.models.adapter.ShoppingCartAdapter
import com.epitech.cashmanager.services.ShoppingCartService
import kotlinx.android.synthetic.main.activity_shopping_cart.*
import kotlinx.android.synthetic.main.custom_action_bar.*


/**
 * ShoppingCartActivity
 *
 * This class permit to manage different payment methods
 *
 * @property ShoppingCartAdapter the adapter permit to manage date of cart (quantity, price, etc..)
 */

class ShoppingCartActivity : AppCompatActivity() {

    lateinit var adapter: ShoppingCartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_cart)
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary))
        swipeRefreshLayout.isRefreshing = true
        swipeRefreshLayout.setOnRefreshListener {
            starting()
        }
        val bundle = intent.extras
        val conn = bundle.getString("val_conn")
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.custom_action_bar)
        StateNetwork.text = getString(R.string.status_2)
        if(conn == "Connected") {
            StateNetwork.setTextColor(Color.GREEN)
        }
        starting()
        paiementButton.setOnClickListener{
            val intent = Intent(this, PaymentActivity::class.java)
            startActivity(intent)
        }
    }

    private fun starting() {
        swipeRefreshLayout.isRefreshing = false
        adapter = ShoppingCartAdapter(this, ShoppingCartService.getCart())
        adapter.notifyDataSetChanged()
        shopping_cart_recyclerView.adapter = adapter
        shopping_cart_recyclerView.layoutManager = LinearLayoutManager(this)
        val totalPrice = ShoppingCartService.getCart()
            .fold(0.toDouble()) { acc, cartItem -> acc + cartItem.quantity.times(cartItem.product.price!!.toDouble()) }
        total_price.text = "${totalPrice}€"
    }

    fun deleteAll(view: View) {
        ShoppingCartService.clearCart()
        adapter = ShoppingCartAdapter(this, ShoppingCartService.getCart())
        adapter.notifyDataSetChanged()
        setContentView(R.layout.activity_shopping_cart)
    }
}
