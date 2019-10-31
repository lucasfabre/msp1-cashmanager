package com.epitech.cashmanager.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ScanActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    private var mScannerView: ZXingScannerView? = null

    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        title = "Paiement QrCode"
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

    override fun handleResult(rawResult: Result) {
        /*val builder = AlertDialog.Builder(this)
        builder.setTitle("Scan Result")
        builder.setMessage(qrLinks.text)
        val alert1 = builder.create()
        alert1.show()*/
        Log.v("TAG", rawResult.text)
        Log.v(
            "TAG",
            rawResult.barcodeFormat.toString()
        )
        //****Start QRCode Link INTO CHROME****//
        val uris = Uri.parse(rawResult.text)
        val intents = Intent(Intent.ACTION_VIEW, uris)
        val b = Bundle()
        b.putBoolean("new_window", true)
        intents.putExtras(b)
        startActivity(intents)
        //mScannerView?.resumeCameraPreview(this)
    }
}
