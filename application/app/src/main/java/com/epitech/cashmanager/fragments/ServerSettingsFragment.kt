package com.epitech.cashmanager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.epitech.cashmanager.R
import com.epitech.cashmanager.models.ServerSettingsViewModel
import com.epitech.cashmanager.services.ServerSettingsService
import com.epitech.cashmanager.services.SocketService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.KotlinModule
import android.text.TextUtils
import android.view.Gravity
import com.sdsmdg.tastytoast.TastyToast
import org.json.JSONArray
import org.json.JSONObject

/**
 * Server settings fragment
 *
 * This class is an instance of Socket
 *
 * @property ServerSettingsViewModel the settingsViewModel define a data model for th settings view
 * @return fragment
 */

class ServerSettingsFragment : Fragment()  {

    private lateinit var settingsViewModel: ServerSettingsViewModel
    private var socketService: SocketService = SocketService()
    private val mapper = ObjectMapper().registerModule(KotlinModule())
    private val settingService: ServerSettingsService = ServerSettingsService()
    private val validateErrors: JSONObject = JSONObject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
            ViewModelProviders.of(this).get(ServerSettingsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_settings, container, false)

        val hostname: EditText = root.findViewById(R.id.hostname)
        val password: EditText = root.findViewById(R.id.password)

        val settings: JSONObject = settingService.getSettings()

        if(settings.length() > 0) {
            if(settings.has("hostname")) {
                hostname.setText(settings.getString("hostname"))
            }
            if(settings.has("password")) {
                password.setText(settings.getString("password"))
            }
        }

        val btnConnexion: Button = root.findViewById(R.id.btnConnexion)

        if(socketService.getSocket().clientSocket != null) {
            if(socketService.getSocket().clientSocket!!.isClosed) {
                btnConnexion.setEnabled(true)
            } else {
                btnConnexion.setEnabled(false)
            }
        }

        btnConnexion.setOnClickListener {
            val errors: JSONArray = JSONArray()
            // Form validation
            if (TextUtils.isEmpty(hostname.getText())) {
                errors.put("Hostname is required")
            }
            if (TextUtils.isEmpty(password.getText())) {
                errors.put("Password is required")
            }
            validateErrors.put("errors", errors)
            val jsonArray = validateErrors.getJSONArray("errors")

            // Form persistance
            val errorsBuild: StringBuilder = java.lang.StringBuilder()
            if(validateErrors.getJSONArray("errors") != null && jsonArray.length() > 0) {
                for (i in 0 until jsonArray.length()) {
                    errorsBuild.append(jsonArray.getString(i))
                    if((i + 1) != jsonArray.length()) {
                        errorsBuild.append("\n")
                    }
                }
                TastyToast.makeText(
                    root.context,
                    errorsBuild.toString(),
                    TastyToast.LENGTH_SHORT,
                    TastyToast.ERROR
                ).setGravity(Gravity.BOTTOM, 0, 150)
                validateErrors.remove("errors")
            } else {
                val saveSettings: CheckBox = root.findViewById(R.id.checkbox)
                if (saveSettings.isChecked) {
                    val settings: JSONObject = JSONObject()
                    settings.put("hostname", hostname.getText().toString())
                    settings.put("password", password.getText().toString())
                    settingService.clearSettings()
                    settingService.saveSettings(root, settings)
                }

                // Socket
                var params: ObjectNode = mapper.createObjectNode()
                params.put("accountId", "acc1")

                socketService.getSocket().start(hostname.getText().toString(), root.context)
                if(socketService.getSocket().clientSocket != null) {
                    socketService.sendRCPFormatData("DescribeAccount", params, 1)
                    btnConnexion.isEnabled = false
                    println("Connected: "+ socketService.isConnected())
                }
            }
        }

        return root
    }

}