package com.epitech.cashmanager.activity

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import com.epitech.cashmanager.R
import com.epitech.cashmanager.services.ShoppingCartService
import com.epitech.cashmanager.services.SocketService
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

    override fun onTagDiscovered(tag: Tag?) {
        val isoDep = IsoDep.get(tag)
        isoDep.connect()
        /*NFCView.append(
            "\nCard Response: "
                    + Utils.toHex(isoDep.tag.id)
        )*/
        //TODO: place card processing here
        val errors: JSONArray = JSONArray()
        val totalPrice = ShoppingCartService.getCart()
            .fold(0.toDouble()) { acc, cartItem -> acc + cartItem.quantity.times(cartItem.product.price!!.toDouble()) }
        Thread({
            socketService.getSocket().start("192.168.0.37", this)
        })
        var params: ObjectNode = mapper.createObjectNode()
        params.put("DebtorAccountId", "acc1")
        socketService.sendRCPFormatData("ValidateAndProcessTransaction", params, 1)
        val rcpResult = socketService.getJsonRcpObject()
        println("###########" + rcpResult)
        if (rcpResult.get("result").has("Amount")) {
            TastyToast.makeText(
                this,
                rcpResult.get("result").get("Amount").toString(),
                TastyToast.LENGTH_SHORT,
                TastyToast.INFO
            ).setGravity(Gravity.BOTTOM, 0, 150)
        } else {
            params = mapper.createObjectNode()
            params.put("CreditorAccountId", "acc2")
            params.put("Amount", totalPrice)
            socketService.sendRCPFormatData("StartTransaction", params, 2)
            val result = socketService.getJsonRcpObject()
            if (result.get("result").asText("success")  == "false") {
                errors.put("Payment denied")
            }
            println("###########" + result)
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
                NFCView.context,
                errorsBuild.toString(),
                TastyToast.LENGTH_SHORT,
                TastyToast.ERROR
            ).setGravity(Gravity.BOTTOM, 0, 150)
            validateErrors.remove("errors")
        }
        isoDep.close()
        finish()
    }
}