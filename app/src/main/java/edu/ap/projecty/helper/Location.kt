package edu.ap.projecty.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.location.Location as AndroidLocation
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.*

class LocationHelper(
    private val context: Context,
    private val activity: FragmentActivity,
    private val permissionRequestCode: Int,
    private val onLocationReceived: (String, String) -> Unit,
    private val onPermissionDenied: () -> Unit
) {

    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)

    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 10000 // 10 seconds
        fastestInterval = 5000 // 5 seconds
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location: AndroidLocation? = locationResult.lastLocation
            if (location != null) {
                Log.i("LocationHelper", "Received location: Latitude = ${location.latitude}, Longitude = ${location.longitude}, Accuracy = ${location.accuracy}, Time = ${location.time}")
                val latitude = location.latitude.toString()
                val longitude = location.longitude.toString()
                onLocationReceived(latitude, longitude)
                stopLocationUpdates()
            } else {
                Log.i("LocationHelper", "Location is null")
                onLocationReceived("Unknown", "Unknown")
            }
        }
    }

    fun getUserLocation() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates()
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), permissionRequestCode)
        }
    }

    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                .addOnFailureListener { e ->
                    Log.e("LocationHelper", "Failed to request location updates", e)
                    onLocationReceived("Unknown", "Unknown")
                }
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
            .addOnFailureListener { e ->
                Log.e("LocationHelper", "Failed to stop location updates", e)
            }
    }

    fun handlePermissionsResult(requestCode: Int, grantResults: IntArray) {
        if (requestCode == permissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else {
                onPermissionDenied()
            }
        }
    }
}
