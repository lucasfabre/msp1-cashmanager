package com.epitech.cashmanager.repositories

import io.paperdb.Paper
import org.json.JSONObject

/**
 * Server settings repository
 *
 * This class represent server settings persistance
 */

class ServerSettingsRepository {

    fun saveSettings(settings: JSONObject) {
        Paper.book().write("settings", settings)
    }

    fun getSettings(): JSONObject {
        return Paper.book().read("settings", JSONObject())
    }

}