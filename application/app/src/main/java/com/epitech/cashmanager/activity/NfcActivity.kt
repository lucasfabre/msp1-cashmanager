package com.epitech.cashmanager.activity

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.epitech.cashmanager.R
import com.epitech.cashmanager.tools.Utils
import kotlinx.android.synthetic.main.activity_nfcpay.*

/**
 * NfcActivity
 *
 * This class permit to manage NFC view
 *
 * @property NfcAdapter the nfcAdapter represent the default NFC hardware access
 */

class NfcActivity : AppCompatActivity(), NfcAdapter.ReaderCallback {

    private var nfcAdapter: NfcAdapter? = null

    override fun onTagDiscovered(tag: Tag?) {
        val isoDep = IsoDep.get(tag)
        isoDep.connect()
        runOnUiThread {
            NFCView.append(
                "\nCard Response: "
                        + Utils.toHex(isoDep.tag.id)
            )
        }
        isoDep.close()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Payment NFC"
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