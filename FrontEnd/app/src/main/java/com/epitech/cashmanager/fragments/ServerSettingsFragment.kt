package com.epitech.cashmanager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.epitech.cashmanager.R
import com.epitech.cashmanager.models.ServerSettingsViewModel

class ServerSettingsFragment : Fragment() {

    private lateinit var parameterViewModel: ServerSettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        parameterViewModel =
            ViewModelProviders.of(this).get(ServerSettingsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_parameter, container, false)
        /*val textView: TextView = root.findViewById(R.id.text_parameter)
        parameterViewModel.text.observe(this, Observer {
            textView.text = it
        })*/
        return root
    }
}