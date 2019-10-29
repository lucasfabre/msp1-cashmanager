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

class ServerSettingsFragment : Fragment()  {

    private lateinit var settingsViewModel: ServerSettingsViewModel

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
        }

        return root
    }

}