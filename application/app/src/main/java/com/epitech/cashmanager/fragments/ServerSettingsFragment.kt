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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
            ViewModelProviders.of(this).get(ServerSettingsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        /* settingsViewModel.hostname.observe(this, Observer {
            hostname.setText(it)
        }) */

        val hostname: EditText = root.findViewById(R.id.hostname)
        if (TextUtils.isEmpty(hostname.getText())) {
            TastyToast.makeText(
                root.context,
                "Hostname is required",
                TastyToast.LENGTH_SHORT,
                TastyToast.INFO
            ).setGravity(Gravity.TOP, 0, 150)
        }

        val password: EditText = root.findViewById(R.id.password)
        if (TextUtils.isEmpty(password.getText())) {
            TastyToast.makeText(
                root.context,
                "Password is required",
                TastyToast.LENGTH_SHORT,
                TastyToast.INFO
            ).setGravity(Gravity.TOP, 0, 250)
        }

        val saveSettings: CheckBox = root.findViewById(R.id.checkbox);
        if (saveSettings.isChecked) {
            settingService.saveData(root)
        }

        val btnConnexion: Button = root.findViewById(R.id.btnConnexion)
        btnConnexion.text = "Connexion"
        btnConnexion.setOnClickListener {
            btnConnexion.isEnabled = false

            var params: ObjectNode = mapper.createObjectNode()
            params.put("accountId", "acc1")
            socketService.getSocket().start()
            socketService.sendRCPFormatData("DescribeAccount", params, 1)
            println(socketService.getJsonRcpObject())
            println(socketService.isConnected())
            btnConnexion.isEnabled = true
        }

        return root
    }

}