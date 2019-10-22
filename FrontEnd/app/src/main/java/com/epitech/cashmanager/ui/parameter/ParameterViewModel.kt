package com.epitech.cashmanager.ui.parameter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ParameterViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is parameter Fragment"
    }
    val text: LiveData<String> = _text
}