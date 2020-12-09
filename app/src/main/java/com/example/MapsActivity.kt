package com.example

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.api.EndPoints
import com.example.api.OutputPost
import com.example.api.Problema
import com.example.api.ServiceBuilder
import com.example.trabalho1.R
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, SensorEventListener {


    private lateinit var mMap: GoogleMap
    private lateinit var problems: List<Problema>
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var continenteLat: Double = 0.0
    private var continenteLong: Double = 0.0
    private lateinit var lat: String
    private lateinit var lon: String
    var sensor : Sensor? = null
    var sensorManager : SensorManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //initialize fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        var call = request.getProblema()
        var position: LatLng

        call.enqueue(object : retrofit2.Callback<List<Problema>> {
            override fun onResponse(
                call: Call<List<Problema>>,
                response: Response<List<Problema>>
            ) {
                if (response.isSuccessful) {
                    problems = response.body()!!
                    for (problem in problems) {
                        position = LatLng(
                            problem.lat.toString().toDouble(),
                            problem.lon.toString().toDouble()
                        )
                        mMap.addMarker(
                            MarkerOptions().position(position).title(problem.descr.toString())
                        )
                    }

                }
            }

            override fun onFailure(call: Call<List<Problema>>, t: Throwable) {

                Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
                var loc = LatLng(lastLocation.latitude, lastLocation.longitude)
                lat = loc.latitude.toString()
                lon = loc.longitude.toString()
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15.0f))
                findViewById<TextView>(R.id.txtcoordenadas).text =
                    "Lat: " + loc.latitude + " - Long: " + loc.longitude
                Log.d(
                    "**** Andre",
                    "new location received - " + loc.latitude + " -" + loc.longitude
                )
                val address = getAddress(lastLocation.latitude, lastLocation.longitude)
                findViewById<TextView>(R.id.txtmorada).setText("Morada: " + address)
                findViewById<TextView>(R.id.txtdistancia).setText(
                    "Distância: " + calculateDistance(
                        lastLocation.latitude, lastLocation.longitude,
                        continenteLat, continenteLong
                    ).toString()
                )

            }
        }
        createLocationRequest()
        continenteLat = 41.7043
        continenteLong = -8.8148


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setUpMap()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        } else {
            //1
            mMap.isMyLocationEnabled = true
            //2
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->

                if (location != null) {
                    lastLocation = location
                    Toast.makeText(this@MapsActivity, lastLocation.toString(), Toast.LENGTH_SHORT)
                        .show()
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                }

            }
        }

    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
// interval specifies the rate at which your app will like to receive updates.
        locationRequest.interval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
        fusedLocationClient.removeLocationUpdates(locationCallback)
        Log.d("**** Andre", "onPause - removeLocationUpdates")
    }

    public override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL)
        startLocationUpdates()
        Log.d("**** Andre", "onResume - startLocationUpdates")
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null /* Looper */
        )
    }

    private fun getAddress(lat: Double, lng: Double): String {
        val geocoder = Geocoder(this)
        val list = geocoder.getFromLocation(lat, lng, 1)
        return list[0].getAddressLine(0)
    }

    fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lng1, lat2, lng2, results)
// distance in meter
        return results[0]
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menumaps, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {

            R.id.logout -> {
                var token = getSharedPreferences("username", Context.MODE_PRIVATE)
                var editor = token.edit()
                editor.putString(
                    "username_login_atual",
                    " "
                )        // Iguala valor a vazio, fica sem valor, credenciais soltas
                editor.commit()                                     // Atualizar editor
                val intent = Intent(this@MapsActivity, LoginActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.AddMarker -> {
                val intent2 = Intent(this, AddMarkerActivity::class.java)
                intent2.putExtra("latitude", lat)
                intent2.putExtra("longitude", lon)
                startActivity(intent2)
                true
            }
            // R.id.removeMarker-> {
            //   val intent3=  Intent(this, RemoveMarkerActivity::class.java)
            //  intent3.putExtra("")
            //  true
            // }
            else -> super.onOptionsItemSelected(item)
        }

    }


  override fun onSensorChanged(event: SensorEvent?) {
        try {

            if (event!!.values[0] < 30) {


                try {
                    // Customise the styling of the base map using a JSON object defined
                    // in a raw resource file.
                    val success = mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle))
                    if (!success) {
                        Log.e("MapsActivity", "Style parsing failed.")
                    }
                } catch (e: Resources.NotFoundException) {
                    Log.e("MapsActivity", "Can't find style. Error: ", e)
                }



            } else {

                findViewById<FrameLayout>(R.id.map).visibility = View.VISIBLE
                try {
                    // Customise the styling of the base map using a JSON object defined
                    // in a raw resource file.
                    val success = mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle2))
                    if (!success) {
                        Log.e("MapsActivity", "Style parsing failed.")
                    }
                } catch (e: Resources.NotFoundException) {
                    Log.e("MapsActivity", "Can't find style. Error: ", e)
                }

            }

        }
        catch (e : Exception)
        {

        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }







}
