package com.epitech.cashmanager.tools

import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Socket Instance
 *
 * This class is an instance of Socket
 *
 * @property Activity activity represent the activity for which manage permission
 * @property List<String> list represent the list of permissions
 * @property Int code represent the code of permission
*/

class ManagePermissions(val activity: Activity, val list: List<String>, val code: Int) {

    /**
     * checkPermissions
     *
     * This method check permissions at runtime
     */

    fun checkPermissions() {
        if (isPermissionsGranted() != PackageManager.PERMISSION_GRANTED) {
            showAlert()
        } else {

        }
    }

    /**
     * isPermissionsGranted
     *
     * This method check permissions status
     * @return Int
     */

    private fun isPermissionsGranted(): Int {
        var counter = 0
        for (permission in list) {
            counter += ContextCompat.checkSelfPermission(activity, permission)
        }
        return counter
    }

    /**
     * deniedPermission
     *
     * This method find the first denied permission
     * @return String
     */

    private fun deniedPermission(): String {
        for (permission in list) {
            if (ContextCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_DENIED
            ) return permission
        }
        return ""
    }

    /**
     * showAlert
     *
     * This method show alert dialog to request permissions
     */

    private fun showAlert() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Need permission(s)")
        builder.setMessage("Some permissions are required to do the task.")
        builder.setPositiveButton("OK", { dialog, which -> requestPermissions() })
        builder.setNeutralButton("Cancel", null)
        val dialog = builder.create()
        dialog.show()
    }

    /**
     * requestPermissions
     *
     * This method request the permissions at run time
     */

    private fun requestPermissions() {
        val permission = deniedPermission()
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            // Show an explanation asynchronously

        } else {
            ActivityCompat.requestPermissions(activity, list.toTypedArray(), code)
        }
    }

    /**
     * processPermissionsResult
     *
     * This method return process permissions result with an Boolean
     * @return Boolean
     */

    fun processPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ): Boolean {
        var result = 0
        if (grantResults.isNotEmpty()) {
            for (item in grantResults) {
                result += item
            }
        }
        if (result == PackageManager.PERMISSION_GRANTED) return true
        return false
    }

}