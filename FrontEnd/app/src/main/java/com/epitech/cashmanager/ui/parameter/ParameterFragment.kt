package com.epitech.cashmanager.ui.parameter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.epitech.cashmanager.R

class ParameterFragment : Fragment() {

    private lateinit var parameterViewModel: ParameterViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        parameterViewModel =
            ViewModelProviders.of(this).get(ParameterViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_parameter, container, false)
        /*val textView: TextView = root.findViewById(R.id.text_parameter)
        parameterViewModel.text.observe(this, Observer {
            textView.text = it
        })*/
        return root
    }
}