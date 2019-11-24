package com.epitech.cashmanager.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.epitech.cashmanager.R
import com.epitech.cashmanager.services.ShoppingCartService
import com.epitech.cashmanager.tools.ManagePermissions
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.paperdb.Paper
import kotlinx.android.synthetic.main.custom_action_bar.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


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

    @SuppressLint("ClickableViewAccessibility")
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

    @SuppressLint("SetTextI18n")
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
        showCart.setOnTouchListener(this)
        cart_size.text = ShoppingCartService.getShoppingCartSize().toString()
        val list = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET
        )
        managePermissions = ManagePermissions(this, list, PermissionsRequestCode)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            managePermissions.checkPermissions()
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        if (hasInternetAccess(this)){
            StateNetwork.text = getString(R.string.status_2)
            StateNetwork.setTextColor(Color.GREEN)
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

    fun hasInternetAccess(context: Context?): Boolean {
            try {
                val urlc: HttpURLConnection = URL("http://clients3.google.com/generate_204")
                    .openConnection() as HttpURLConnection
                urlc.setRequestProperty("User-Agent", "Android")
                urlc.setRequestProperty("Connection", "close")
                urlc.connectTimeout = 1500
                urlc.connect()
                return urlc.responseCode === 204 &&
                        urlc.contentLength === 0
            } catch (e: IOException) {
                Log.e("Error", e.message)
            }
        return false
    }
}
