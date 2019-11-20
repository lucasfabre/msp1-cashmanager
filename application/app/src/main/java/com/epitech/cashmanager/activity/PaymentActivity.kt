package com.epitech.cashmanager.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.epitech.cashmanager.R
import com.epitech.cashmanager.models.adapter.ShoppingCartAdapter
import com.epitech.cashmanager.services.ShoppingCartService
import kotlinx.android.synthetic.main.activity_paiement.*

/**
 * PaymentActivity
 *
 * This class permit to manage different payment methods
 *
 * @property ShoppingCartAdapter the adapter permit to manage date of cart (quantity, price, etc..)
 */

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

        val nfcIntent: Button = findViewById(R.id.nfc)
        nfcIntent.setOnClickListener {
            goToNFC(it)
        }

        val qrCodeIntent: Button = findViewById(R.id.qrCode)
        qrCodeIntent.setOnClickListener {
            goToQrCode(it)
        }
    }

    /**
     * goToNFC
     *
     * This method called an intent for will launch NFC reader
     *
     * @param View view represent currentView
     */

    fun goToNFC(view: View) {
        val intent = Intent(this, NfcActivity::class.java)
        //finish() //close current activity
        startActivity(intent)
    }

    /**
     * goToQrCode
     *
     * This method called an intent for will launch QR Code
     *
     * @param View view represent currentView
     */

    fun goToQrCode(view: View) {
        val intent = Intent(this, ScanActivity::class.java)
        //finish() //close current activity
        startActivity(intent)
    }

}
