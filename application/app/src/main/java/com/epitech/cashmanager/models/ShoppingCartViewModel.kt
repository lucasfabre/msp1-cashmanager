package com.epitech.cashmanager.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShoppingCartViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is shoppingCart Fragment"
    }
    val text: LiveData<String> = _text

}