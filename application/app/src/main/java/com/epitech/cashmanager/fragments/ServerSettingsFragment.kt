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
import com.sdsmdg.tastytoast.TastyToast;
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
    private val settings: JSONObject ?= null
    private val errors: JSONArray = JSONArray()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
            ViewModelProviders.of(this).get(ServerSettingsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_settings, container, false)

        if(settings == null) {
            val settings: JSONObject = settingService.getSettings()
        }

        val validateErrors: JSONObject = JSONObject()

        val hostname: EditText = root.findViewById(R.id.hostname)
        if(settings != null) {
            hostname.setText(settings.getString("hostname"))
        }

        val password: EditText = root.findViewById(R.id.password)
        if(settings != null) {
            password.setText(settings.getString("password"))
        }

        val btnConnexion: Button = root.findViewById(R.id.btnConnexion)
        btnConnexion.setOnClickListener {
            btnConnexion.isEnabled = false

            // Form validation
            if (TextUtils.isEmpty(hostname.getText())) {
                errors.put("Hostname is required" + "\n")
            }
            if (TextUtils.isEmpty(password.getText())) {
                errors.put("Password is required")
            }
            validateErrors.put("errors", errors)

            // Form persistance
            if(!validateErrors.equals(null)) {
                val saveSettings: CheckBox = root.findViewById(R.id.checkbox)
                if (saveSettings.isChecked) {
                    val settings: JSONObject = JSONObject()
                    settings.put("hostname", hostname.getText().toString())
                    settings.put("password", password.getText().toString())

                    settingService.saveSettings(root, settings)
                }
            } else {
                TastyToast.makeText(
                    root.context,
                    validateErrors.getJSONArray("errors").toString(),
                    TastyToast.LENGTH_SHORT,
                    TastyToast.INFO
                ).setGravity(Gravity.TOP, 0, 150)
            }

            // Socket
            /*var params: ObjectNode = mapper.createObjectNode()
            params.put("accountId", "acc1")
            socketService.getSocket().start()
            socketService.sendRCPFormatData("DescribeAccount", params, 1)
            println(socketService.getJsonRcpObject())
            println(socketService.isConnected())
            btnConnexion.isEnabled = true*/
        }

        return root
    }

}