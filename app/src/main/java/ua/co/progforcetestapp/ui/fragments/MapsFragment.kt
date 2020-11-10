package ua.co.progforcetestapp.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import pub.devrel.easypermissions.EasyPermissions
import ua.co.progforcetestapp.R
import ua.co.progforcetestapp.utility.Constants.LOCATION_REQUEST_CODE
import ua.co.progforcetestapp.utility.TrackingUtility


@AndroidEntryPoint
class MapsFragment : Fragment(R.layout.fragment_maps), OnMapReadyCallback, EasyPermissions.PermissionCallbacks{
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var latitude: Double = -34.0
    private var longitude: Double = 151.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        askLocationPermission()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            return
        }
        lifecycleScope.launch {
            fusedLocationProviderClient.lastLocation.await()?.let { location ->
                val sydney = LatLng(location.latitude, location.longitude)
                map.addMarker(MarkerOptions().position(sydney).title("Your Location"))
                map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
            } ?: setDefaultLocation()
        }
    }

    private fun askLocationPermission() {
        if (TrackingUtility.hasLocationPermissions(requireContext())) {  getLastLocation() }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.you_have_to_accept_permission_for_location),
                LOCATION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.you_have_to_accept_permission_for_location),
                LOCATION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    private fun setDefaultLocation(){
        val sydney = LatLng(latitude, longitude)
        map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap ?: return

        map.setOnMapLongClickListener { latLng ->
            map.clear()
            map.addMarker(
                MarkerOptions()
                    .position(latLng!!)
                    .title("New Location1")
                    .draggable(true)
            )
        }

        map.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener{
            override fun onMarkerDragStart(p0: Marker?) = Unit

            override fun onMarkerDrag(p0: Marker?) = Unit

            override fun onMarkerDragEnd(marker: Marker?) {
                map.clear()
                map.addMarker(MarkerOptions()
                    .position(marker?.position!!)
                    .title("New Location")
                    .draggable(true))
                /*latitude = marker.position.latitude
                longitude = marker.position.longitude*/
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        getLastLocation()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        setDefaultLocation()
    }
}