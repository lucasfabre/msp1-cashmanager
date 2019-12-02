package com.epitech.cashmanager.activity

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import com.epitech.cashmanager.R
import com.epitech.cashmanager.services.ShoppingCartService
import com.epitech.cashmanager.services.SocketService
import com.epitech.cashmanager.tools.ManagePermissions
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.zxing.Result
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.activity_nfcpay.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import org.json.JSONArray
import org.json.JSONObject

class ScanActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    private val mapper = ObjectMapper().registerModule(KotlinModule())
    private var mScannerView: ZXingScannerView? = null
    private var socketService: SocketService = SocketService()
    private val validateErrors: JSONObject = JSONObject()

    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        title = getString(R.string.paiement_qrcode)
        mScannerView = ZXingScannerView(this)
        setContentView(mScannerView)
    }

    public override fun onResume() {
        super.onResume()
        mScannerView?.setResultHandler(this)
        mScannerView?.startCamera()
    }

    public override fun onPause() {
        super.onPause()
        mScannerView?.stopCamera()
    }

    /**
     * handleResult
     *
     * This method is use for decode QR Code Image and launch intent
     *
     * @param Result rawResult represent the result of decoding QR Code Image
     */

    override fun handleResult(rawResult: Result) {
        Log.v("TAG", rawResult.text)
        Log.v(
            "TAG",
            rawResult.barcodeFormat.toString()
        )
        //TODO: Place payment processing here with rawResult.text
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
                NFCView.context,
                errorsBuild.toString(),
                TastyToast.LENGTH_SHORT,
                TastyToast.ERROR
            ).setGravity(Gravity.BOTTOM, 0, 150)
            validateErrors.remove("errors")
        }
        finish()
    }
}
