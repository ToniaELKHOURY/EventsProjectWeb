/*
    MapsFragment.kt

    Cette classe, MapsFragment, représente le fragment qui affiche une carte Google Maps.
    Elle permet de visualiser des marqueurs sur la carte en fonction des informations fournies.
*/

package com.example.events_frontend_project

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.ArrayList
import java.util.Locale

class MapsFragment : Fragment() {
    private var userLatitude: Double = 0.0
    private var userLongitude: Double = 0.0
    private var mapsInfo: ArrayList<MapsInfo> = arrayListOf()
    private lateinit var map: GoogleMap
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2

    private val callback = OnMapReadyCallback { googleMap ->


        map = googleMap
        map.uiSettings.isCompassEnabled = true

        if(mapsInfo.size != 0){
            mapsInfo.forEach {
                googleMap.addMarker(MarkerOptions().position(LatLng(it.latitude, it.longitude)).title(it.title))
            }
        }

        getLocation()
    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mapsInfo = it.getSerializable(EVENT) as ArrayList<MapsInfo>
        }

        mFusedLocationClient = requireActivity().let {
            LocationServices.getFusedLocationProviderClient(
                it
            )
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (requireActivity().let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED &&
            requireActivity().let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        requireActivity().let {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                permissionId
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        val list: List<Address> =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)!!

                        userLatitude = list[0].latitude
                        userLongitude = list[0].longitude
                        map.addMarker(MarkerOptions().position(LatLng(userLatitude, userLongitude)).title("Your location"))?.setIcon(
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(userLatitude, userLongitude), 18.0f))
                    }
                }
            } else {
                Toast.makeText(this.context, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        getLocation()
    }

    companion object {
        @JvmStatic
        fun newInstance(mapsInfo: ArrayList<MapsInfo>) =
            MapsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(EVENT , mapsInfo)
                }

            }
    }
}