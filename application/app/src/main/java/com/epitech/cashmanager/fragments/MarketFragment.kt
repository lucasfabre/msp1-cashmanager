package com.epitech.cashmanager.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.epitech.cashmanager.R
import com.epitech.cashmanager.beans.Product
import com.epitech.cashmanager.models.adapter.ProductAdapter
import kotlinx.android.synthetic.main.fragment_market.*
import java.io.InputStream

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
        products_recyclerview.apply {
            layoutManager = GridLayoutManager(context, 2)
        }
        getProducts()
    }

    private fun getProducts() {
        val product = Product("basic description", 1, "Huawei P30", "460", "https://cdn.shopify.com/s/files/1/0158/2612/4864/products/1_608x608.jpg?v=1566905322")
        val product2 = Product("basic description", 2, "Samsung S9", "370","https://image.darty.com/darty?type=image&source=/market/2018/04/11/mkp_L-5ioJQATYOuog.jpeg&width=269&height=405&quality=95" )
        val product3 = Product("basic description", 3, "Samsung S10", "600", "https://csmobiles.com/15710-large_default/samsung-galaxy-s10-g973f-128go-dual-sim-bleu.jpg")
        val product4 = Product("basic description", 4, "Iphone 11 Pro", "1100", "https://static.fnac-static.com/multimedia/Images/FR/MDM/7a/b2/bd/12431994/1505-1/tsp20191113133346/Apple-iPhone-11-Pro-64-Go-5-8-Gris-Sideral.jpg")
        val product5 = Product("basic description", 5, "Surface Pro X", "1549", "https://img-prod-cms-rt-microsoft-com.akamaized.net/cms/api/am/imageFileData/RE3onLg?ver=3fa8&q=90&m=6&h=705&w=1253&b=%23FFF0F0F0&f=jpg&o=f&p=140&aim=true")
        val product6 = Product("basic description", 6, "MacBook Pro", "3100", "https://images.fr.shopping.rakuten.com/photo/1141349434.jpg")

        products.add(product)
        products.add(product2)
        products.add(product3)
        products.add(product4)
        products.add(product5)
        products.add(product6)
        productAdapter = context?.let { ProductAdapter(it, products) }!!
        products_recyclerview.adapter = productAdapter
    }

}
