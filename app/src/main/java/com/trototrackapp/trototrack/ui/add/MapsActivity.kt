package com.trototrackapp.trototrack.ui.add

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.trototrackapp.trototrack.R
import com.trototrackapp.trototrack.databinding.ActivityMapsBinding
import java.io.IOException
import java.util.Locale

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var centerMarker: Marker
    private lateinit var locationAddressTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationAddressTextView = findViewById(R.id.locationAddress)

        val buttonDone = findViewById<Button>(R.id.selectLocationButton)
        buttonDone.setOnClickListener {
            sendLocationData()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        getMyLocation()

        mMap.setOnMapClickListener { latLng ->
            if (!::centerMarker.isInitialized) {
                centerMarker = mMap.addMarker(MarkerOptions().position(latLng).title("Selected Location"))!!
            } else {
                centerMarker.position = latLng
            }
            updateLocationAddress(latLng)
        }
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun updateLocationAddress(latLng: LatLng) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0].getAddressLine(0)
                locationAddressTextView.text = address
            } else {
                locationAddressTextView.text = "No address found"
            }
        } catch (e: IOException) {
            e.printStackTrace()
            locationAddressTextView.text = "Unable to get address"
        }
    }

    private fun sendLocationData() {
        val locationName = locationAddressTextView.text.toString()
        val latLng = centerMarker.position

        val resultIntent = Intent().apply {
            putExtra("LOCATION_NAME", locationName)
            putExtra("LATITUDE", latLng.latitude)
            putExtra("LONGITUDE", latLng.longitude)
        }

        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}
