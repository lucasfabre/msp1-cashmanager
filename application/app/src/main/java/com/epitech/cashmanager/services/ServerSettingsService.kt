package com.epitech.cashmanager.services

import android.view.View
import android.widget.Toast

/**
 * Server settings service
 *
 * This class is use for define JSON-RCP communication with a server
 */

class ServerSettingsService {

    fun saveData(view: View) {
        Toast.makeText(view.context, "Your datas as been saved", Toast.LENGTH_SHORT).show()
    }

}