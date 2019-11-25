package com.epitech.cashmanager.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.epitech.cashmanager.R
import com.epitech.cashmanager.tools.ManagePermissions
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ScanActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private var mScannerView: ZXingScannerView? = null

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
        //***Example display message***//
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
        //TODO: Place payment processing here with rawResult.text
        //mScannerView?.resumeCameraPreview(this)
    }
}
