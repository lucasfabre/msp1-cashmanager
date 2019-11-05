package com.epitech.cashmanager.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ServerSettingsViewModel : ViewModel() {

    private val _hostname = MutableLiveData<String>().apply {
        value = "192.168.0.23"
    }
    val hostname: LiveData<String> = _hostname

}