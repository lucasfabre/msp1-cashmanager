package com.epitech.cashmanager.ui.panier

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.epitech.cashmanager.R

class PanierFragment : Fragment() {

    private lateinit var panierViewModel: PanierViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        panierViewModel =
            ViewModelProviders.of(this).get(PanierViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_panier, container, false)
        val textView: TextView = root.findViewById(R.id.text_panier)
        panierViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}