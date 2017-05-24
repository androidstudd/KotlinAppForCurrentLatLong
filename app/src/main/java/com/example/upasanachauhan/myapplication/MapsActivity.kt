package com.example.upasanachauhan.myapplication

import android.annotation.SuppressLint
import android.location.Location
import android.support.v4.app.FragmentActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : FragmentActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{

    private var mMap: GoogleMap? = null
    var googleApiClient: GoogleApiClient? = null
    var mLocationRequest: LocationRequest? = null
    var modifiedLatitude: String? = null
    var modifiedLongitude: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        googleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

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


    }

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {

        val location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this)

        } else {
            //If everything went fine lets get latitude and longitude
            modifiedLatitude = location.getLatitude().toString()
            modifiedLongitude = location.getLongitude().toString()

            Toast.makeText(this,"current latitude is "+modifiedLatitude.toString()+ " and longitude is "+ modifiedLongitude.toString(),Toast.LENGTH_LONG).show()

            if(mMap != null) {
                // Add a marker in Sydney and move the camera
                val sydney = LatLng(modifiedLatitude!!.toDouble(), modifiedLongitude!!.toDouble())
                mMap!!.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
                mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15F))
            }
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLocationChanged(p0: Location?) {

        modifiedLatitude = p0!!.latitude.toString()
        modifiedLongitude = p0!!.longitude.toString()

        Toast.makeText(this, modifiedLatitude + " WORKS " + modifiedLongitude + "", Toast.LENGTH_LONG).show()

    }

    override fun onResume() {
        super.onResume()
        googleApiClient!!.connect()
    }

    override fun onPause() {
        super.onPause()

        //Disconnect from API onPause()
        if (googleApiClient!!.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this)
            googleApiClient!!.disconnect()
        }
    }
}

