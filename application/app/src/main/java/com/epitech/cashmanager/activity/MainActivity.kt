package com.epitech.cashmanager.activity

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.epitech.cashmanager.R
import com.epitech.cashmanager.services.ShoppingCartService
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Paper.init(this)
        setContentView(R.layout.fragment_home)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_market,
                R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        cart_size.text = ShoppingCartService.getShoppingCartSize().toString()
        showCart.setOnClickListener {
            val intent = Intent(this, ShoppingCartActivity::class.java)
            startActivity(intent)
        }

        var clientSocket = Socket("127.0.0.1", 6666)
        val out = PrintWriter(clientSocket.getOutputStream(), true)
        val `in` = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
    }
}
