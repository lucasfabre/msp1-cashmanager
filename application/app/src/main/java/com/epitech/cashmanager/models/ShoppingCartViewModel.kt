package com.epitech.cashmanager.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShoppingCartViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is panier Fragment"
    }
    val text: LiveData<String> = _text
}