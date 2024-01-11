package com.example.weatherapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import com.example.weatherapp.utils.LocationPermissionWrapper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    private val locationPermissionWrapper: LocationPermissionWrapper by lazy {
        LocationPermissionWrapper(this, R.string.app_name)
    }
    private var checkLocation = false

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handleLocationRequirements()
    }

    override fun onResume() {
        super.onResume()

        if (checkLocation) {
            checkLocation = false
            handleLocationRequirements()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionWrapper.onRequestPermissionsResult(requestCode)
    }

    private fun handleLocationRequirements() {
        if (!isLocationEnabled(getSystemService(Context.LOCATION_SERVICE) as LocationManager)) {
            showLocationDisabledDialog()
            return
        }

        checkLocationPermission()
    }

    // Only a simplified location handling as of now
    @SuppressLint("MissingPermission")
    private fun checkLocationPermission() {
        locationPermissionWrapper.requestPermission(
            onPermissionGranted = {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        location?.run {

                        }
                    }
            },
            onPermissionDenied = { },
            onPermissionPermanentlyDenied = { })
    }

    // Will add Compose later
    private fun showLocationDisabledDialog() {
        AlertDialog.Builder(this)
            .setTitle("")
            .setMessage("We need to access your location. Please enable it from the Settings page!")
            .setCancelable(false)
            .setNegativeButton("Not now") { d, _ ->
                finish()
                d.dismiss()
            }
            .setPositiveButton("Settings") { d, _ ->
                d.dismiss()
                val viewIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(viewIntent)
                checkLocation = true
            }
            .create()
            .show()
    }
}