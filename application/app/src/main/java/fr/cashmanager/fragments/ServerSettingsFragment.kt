package fr.cashmanager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import fr.cashmanager.R
import fr.cashmanager.models.ServerSettingsViewModel

class ServerSettingsFragment : Fragment() {

    private val viewModel: ServerSettingsViewModel by viewModels(factoryProducer = { SavedStateVMFactory(this) })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.server_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val login = viewModel.login
        val password = viewModel.password
    }
}