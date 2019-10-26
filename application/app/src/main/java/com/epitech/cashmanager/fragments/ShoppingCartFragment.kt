package com.epitech.cashmanager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.epitech.cashmanager.R
import com.epitech.cashmanager.models.ShoppingCartViewModel

class ShoppingCartFragment : Fragment() {

    private lateinit var shoppingCartViewModel: ShoppingCartViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        shoppingCartViewModel =
            ViewModelProviders.of(this).get(ShoppingCartViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_shopping_cart, container, false)
        val textView: TextView = root.findViewById(R.id.text_shoppingCart)
        shoppingCartViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}