package com.epitech.cashmanager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.epitech.cashmanager.R
import com.epitech.cashmanager.models.ServerSettingsViewModel
import com.epitech.cashmanager.services.SocketService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.KotlinModule

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
            ViewModelProviders.of(this).get(ServerSettingsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        val hostname: EditText = root.findViewById(R.id.text_settings)
        settingsViewModel.hostname.observe(this, Observer {
            hostname.setText(it)
        })

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
        }

        return root
    }

}