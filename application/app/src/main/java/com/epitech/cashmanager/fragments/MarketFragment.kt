package com.epitech.cashmanager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.epitech.cashmanager.R
import com.epitech.cashmanager.models.adapter.ProductAdapter
import com.epitech.cashmanager.services.ShoppingCartService
import kotlinx.android.synthetic.main.fragment_market.*

/**
 * Market fragment
 *
 * This class represent the view of market
 *
 * @property ProductAdapter the productAdapter permit to manage product (add, remove, quantity)
 * @property ArrayList<Product> the products define an string array of products
 */

class MarketFragment : Fragment() {

    private lateinit var productAdapter: ProductAdapter

    companion object {
        fun newInstance() = MarketFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_market, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        products_recyclerview.apply {
            layoutManager = GridLayoutManager(context, 2)
        }
        getProducts()
    }

    private fun getProducts() {
        val products = ShoppingCartService.initProducts()
        productAdapter = context?.let { ProductAdapter(it, products) }!!
        products_recyclerview.adapter = productAdapter
    }

}
