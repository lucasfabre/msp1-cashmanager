package com.epitech.cashmanager.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.epitech.cashmanager.R
import com.epitech.cashmanager.models.adapter.ShoppingCartAdapter
import com.epitech.cashmanager.services.ShoppingCartService
import kotlinx.android.synthetic.main.activity_shopping_cart.*

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
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.custom_action_bar)
        adapter = ShoppingCartAdapter(this, ShoppingCartService.getCart())
        adapter.notifyDataSetChanged()

        shopping_cart_recyclerView.adapter = adapter
        shopping_cart_recyclerView.layoutManager = LinearLayoutManager(this)

        val totalPrice = ShoppingCartService.getCart()
            .fold(0.toDouble()) { acc, cartItem -> acc + cartItem.quantity.times(cartItem.product.price!!.toDouble()) }
        total_price.text = "${totalPrice}€"
        paiementButton.setOnClickListener{
            val intent = Intent(this, PaymentActivity::class.java)
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
