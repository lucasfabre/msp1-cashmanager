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
import com.epitech.cashmanager.services.ShoppingCartService
import com.epitech.cashmanager.tools.ManagePermissions
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_home.*
import android.os.StrictMode
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import com.epitech.cashmanager.R

/**
 * MainActivity
 *
 * This class serve to define the start point of an Android Application
 *
 * @property PermissionsRequestCode the permissionsRequestCode define the number of the permission
 * @property managePermissions the managePermissions permit to manage all android permissions access
 */

class MainActivity : AppCompatActivity(), View.OnTouchListener{

    private val PermissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions
    var dX:Float = 0.toFloat()
    var dY:Float = 0.toFloat()

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                dX = v?.x!! - event.rawX
                dY = v.y - event.rawY
            }
            MotionEvent.ACTION_MOVE -> {

                v?.animate()?.x(event.rawX + dX)?.y(event.rawY + dY)?.setDuration(0)
                    ?.start()
            }
            MotionEvent.ACTION_UP -> {
                val intent = Intent(this, ShoppingCartActivity::class.java)
                startActivity(intent)
            }
            else -> return false
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Paper.init(this)
        setContentView(R.layout.fragment_home)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.custom_action_bar)
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
        /*showCart.setOnClickListener {
            val intent = Intent(this, ShoppingCartActivity::class.java)
            startActivity(intent)
        }*/
        showCart.setOnTouchListener(this)
        cart_size.text = ShoppingCartService.getShoppingCartSize().toString()
        val list = listOf<String>(
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET
        )
        managePermissions = ManagePermissions(this, list, PermissionsRequestCode)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            managePermissions.checkPermissions()
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
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
