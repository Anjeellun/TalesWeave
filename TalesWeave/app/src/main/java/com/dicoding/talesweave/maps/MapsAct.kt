package com.dicoding.talesweave.maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import com.dicoding.talesweave.R
import com.dicoding.talesweave.data.UIState
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.dicoding.talesweave.databinding.ActivityMapsBinding
import com.dicoding.talesweave.viewmodel.MapsVM
import com.dicoding.talesweave.viewmodel.VMFactory

class MapsAct : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var toolbar: Toolbar
    private val MapsVM by viewModels<MapsVM> {
        VMFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = null

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true


        addMarkers()

    }

    private fun addMarkers() {
        MapsVM.getStoryLoc().observe(this) { story ->
            if (story != null) {
                when (story) {
                    is UIState.Loading -> {

                        Log.d("MapsAct", "Loading...")
                    }

                    is UIState.Success -> {
                        val dataStory = story.data
                        dataStory.forEach { location ->
                            val latLng = location.lat?.let {
                                location.lon?.let { it1 ->
                                    LatLng(it, it1)
                                }
                            }
                            latLng?.let { MarkerOptions().position(it).title(location.name) }
                                ?.let { mMap.addMarker(it) }
                        }
                    }

                    is UIState.Error -> {

                        Log.e("MapsAct", "Error: ${story.error}")
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }

            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }

            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }

            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}
