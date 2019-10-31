package com.epitech.cashmanager.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.epitech.cashmanager.R
import com.epitech.cashmanager.models.adapter.ShoppingCartAdapter
import com.epitech.cashmanager.services.ShoppingCartService
import kotlinx.android.synthetic.main.activity_paiement.*

class PaymentActivity : AppCompatActivity() {
    lateinit var adapter: ShoppingCartAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paiement)
        title = "Payment"
        adapter = ShoppingCartAdapter(this, ShoppingCartService.getCart())
        adapter.notifyDataSetChanged()
        val totalPrice = ShoppingCartService.getCart()
            .fold(0.toDouble()) { acc, cartItem -> acc + cartItem.quantity.times(cartItem.product.price!!.toDouble()) }
        total_price.text = "${totalPrice}€"

    }


    fun goToNFC(view: View) {
        val intent = Intent(this, NfcActivity::class.java)
        //finish() //close current activity
        startActivity(intent)
    }

    fun goToQr(view: View) {
        val intent = Intent(this, ScanActivity::class.java)
        //finish() //close current activity
        startActivity(intent)
    }
}
