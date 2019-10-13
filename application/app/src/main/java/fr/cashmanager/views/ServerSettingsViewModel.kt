package fr.cashmanager.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import fr.cashmanager.models.Settings

class ServerSettingsViewModel( savedStateHandle: SavedStateHandle ) : ViewModel() {
    val server : LiveData<Settings> = TODO()
    val password : LiveData<Settings> = TODO()
}

// ServerSettingsFragment
private val viewModel:  ServerSettingsViewModel by viewModels(
    factoryProducer = { SavedStateVMFactory(this) }
)