package com.epitech.cashmanager.activity

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.epitech.cashmanager.R
import com.epitech.cashmanager.services.ShoppingCartService
import com.epitech.cashmanager.tools.ManagePermissions
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * MainActivity
 *
 * This class serve to define the start point of an Android Application
 *
 * @property PermissionsRequestCode the permissionsRequestCode define the number of the permission
 * @property managePermissions the managePermissions permit to manage all android permissions access
 */

class MainActivity : AppCompatActivity() {

    private val PermissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions

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
        val list = listOf<String>(
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET
        )
        managePermissions = ManagePermissions(this, list, PermissionsRequestCode)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            managePermissions.checkPermissions()
        showCart.setOnClickListener {
            val intent = Intent(this, ShoppingCartActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * onRequestPermissionsResult
     *
     * This method is called for init managePermissions object
     *
     * @param Int requestCode
     * @param Array<String> tab of permissions
     * @param IntArray tab of int grantResults
     */

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PermissionsRequestCode -> {
                val isPermissionsGranted = managePermissions
                    .processPermissionsResult(requestCode, permissions, grantResults)
                if (isPermissionsGranted) {
                } else {
                }
                return
            }
        }
    }

}
