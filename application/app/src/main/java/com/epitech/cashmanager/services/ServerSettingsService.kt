package com.epitech.cashmanager.services

import android.view.Gravity
import android.view.View
import com.epitech.cashmanager.repositories.ServerSettingsRepository
import com.sdsmdg.tastytoast.TastyToast
import io.paperdb.Paper
import org.json.JSONObject

/**
 * Server settings service
 *
 * This class is use for define JSON-RCP communication with a server
 */

class ServerSettingsService {

    private val persist: ServerSettingsRepository = ServerSettingsRepository()

    fun saveSettings(view: View, settings: JSONObject) {
        TastyToast.makeText(
            view.context,
            "Your datas as been saved",
            TastyToast.LENGTH_SHORT,
            TastyToast.SUCCESS
        ).setGravity(Gravity.BOTTOM, 0, 150)
        persist.saveSettings(settings)
    }

    fun getSettings(): JSONObject {
        return persist.getSettings()
    }

    fun clearSettings() {
        persist.clearSettings()
    }

}