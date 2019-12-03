package com.epitech.cashmanager.activity

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.epitech.cashmanager.R
import com.epitech.cashmanager.services.ShoppingCartService
import com.epitech.cashmanager.services.SocketService
import com.epitech.cashmanager.tools.Utils
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.activity_nfcpay.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * NfcActivity
 *
 * This class permit to manage NFC view
 *
 * @property NfcAdapter the nfcAdapter represent the default NFC hardware access
 */

class NfcActivity : AppCompatActivity(), NfcAdapter.ReaderCallback {
    private val mapper = ObjectMapper().registerModule(KotlinModule())
    private var nfcAdapter: NfcAdapter? = null
    private var socketService: SocketService = SocketService()
    private val validateErrors: JSONObject = JSONObject()

    override fun onTagDiscovered(tag: Tag?) {
        val isoDep = IsoDep.get(tag)
        isoDep.connect()
        runOnUiThread {
            /*NFCView.append(
                "\nCard Response: "
                        + Utils.toHex(isoDep.tag.id)
            )*/
            //TODO: place card processing here
            val errors: JSONArray = JSONArray()
            val totalPrice = ShoppingCartService.getCart()
                .fold(0.toDouble()) { acc, cartItem -> acc + cartItem.quantity.times(cartItem.product.price!!.toDouble()) }
            var params: ObjectNode = mapper.createObjectNode()
            params.put("CreditorAccountId", "acc2")
            params.put("Amount", totalPrice)
            socketService.sendRCPFormatData("StartTransaction", params, 1)
            socketService.getJsonRcpObject()
            params = mapper.createObjectNode()
            params.put("DebtorAccountId", "acc1")
            socketService.sendRCPFormatData("ValidateAndProcessTransaction", params, 2)
            val deserializedRep2 = socketService.getJsonRcpObject()
            if (deserializedRep2.get("result").asText("success")  == "false") {
                errors.put("Payment denied")
            }
            validateErrors.put("errors", errors)
            val jsonArray = validateErrors.getJSONArray("errors")
            val errorsBuild: StringBuilder = java.lang.StringBuilder()
            if (validateErrors.getJSONArray("errors") != null && jsonArray.length() > 0) {
                for (i in 0 until jsonArray.length()) {
                    errorsBuild.append(jsonArray.getString(i))
                    if ((i + 1) != jsonArray.length()) {
                        errorsBuild.append("\n")
                    }
                }
                TastyToast.makeText(
                    this,
                    errorsBuild.toString(),
                    TastyToast.LENGTH_SHORT,
                    TastyToast.ERROR
                ).setGravity(Gravity.BOTTOM, 0, 150)
                validateErrors.remove("errors")
            } else {
                TastyToast.makeText(this, "Payment Successful", TastyToast.LENGTH_LONG,
                    TastyToast.SUCCESS)
            }
            isoDep.close()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.paiement_nfc)
        setContentView(R.layout.activity_nfcpay)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
    }

    public override fun onResume() {
        super.onResume()
        nfcAdapter?.enableReaderMode(
            this, this,
            NfcAdapter.FLAG_READER_NFC_A or
                    NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
            null
        )
    }

    public override fun onPause() {
        super.onPause()
        nfcAdapter?.disableReaderMode(this)
    }

}