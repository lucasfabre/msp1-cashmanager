package com.epitech.cashmanager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.epitech.cashmanager.R
import com.epitech.cashmanager.beans.Product
import com.epitech.cashmanager.models.adapter.ProductAdapter
import kotlinx.android.synthetic.main.fragment_market.*

class MarketFragment : Fragment() {

    private lateinit var productAdapter: ProductAdapter

    private var products = ArrayList<Product>()
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
        products_recyclerview.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        getProducts()
    }
    private fun getProducts() {
        val product = Product("basic description", 1, "Asus Rog", "1700", R.drawable.laptop)
        val product2 = Product("basic description", 2, "Vin Rouge", "70", R.drawable.wine_bottle)
        products.add(product)
        products.add(product2)
        productAdapter = context?.let { ProductAdapter(it, products) }!!
        products_recyclerview.adapter = productAdapter
    }
}
