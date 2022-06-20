package me.daffakurnia.android.storyapp.ui

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import me.daffakurnia.android.storyapp.R
import me.daffakurnia.android.storyapp.api.ApiConfig
import me.daffakurnia.android.storyapp.data.AppDataStore
import me.daffakurnia.android.storyapp.data.AuthViewModel
import me.daffakurnia.android.storyapp.data.Stories
import me.daffakurnia.android.storyapp.data.ViewModelFactory
import me.daffakurnia.android.storyapp.databinding.ActivityMapsBinding
import me.daffakurnia.android.storyapp.response.ListStoryItem
import me.daffakurnia.android.storyapp.response.StoriesResponse
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.title_activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val pref = AppDataStore.getInstance(dataStore)
        val authViewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]
        authViewModel.loginToken().observe(this) { token: String? ->
            getAllStories(token)
        }
    }

    private fun getAllStories(token: String?) {
        val bearerToken = HashMap<String, String>()
        bearerToken["Authorization"] = "Bearer $token"

        val client = ApiConfig.getApiService().getMapStories(bearerToken)
        client.enqueue(object : Callback, retrofit2.Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        getStories(responseBody.listStory as List<ListStoryItem>)
                    }
                } else {
                    Log.e(this@MapsActivity.toString(), "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                Log.e(this@MapsActivity.toString(), "onFailure: ${t.message}")
            }

        })
    }

    private fun getStories(listStory: List<ListStoryItem>) {
        val storyList = ArrayList<Stories>()

        for (item in listStory) {
            storyList.add(
                Stories(
                    item.photoUrl,
                    item.name,
                    item.description,
                    item.lat.toString(),
                    item.lon.toString()
                )
            )
        }
        addManyMarker(storyList)
    }

    private val boundsBuilder = LatLngBounds.Builder()

    private fun addManyMarker(storyList: java.util.ArrayList<Stories>) {
        storyList.forEach { stories ->
            val latLng = LatLng(stories.lat!!.toDouble(), stories.lon!!.toDouble())
            mMap.addMarker(
                MarkerOptions().position(latLng).title(stories.name).snippet(stories.description)
            )
            boundsBuilder.include(latLng)
        }
        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        getMyLocation()
        setMapStyle()
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
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

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}