package com.example.garage.views


import android.Manifest
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.core.app.ActivityCompat
import com.example.garage.models.LocationUtils
import com.example.garage.viewModels.LocationViewModel

@Composable
fun TrackLocation(
    locationUtils: LocationUtils,
    locationViewModel: LocationViewModel,
    context: Context,
) {
    // Initialize the permission launcher outside the composable function
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            && permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        ) {
            // Have access to location
            locationUtils.requestLocationUpdates(locationViewModel)
        } else {
            val rationalRequired = ActivityCompat
                .shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) || ActivityCompat
                .shouldShowRequestPermissionRationale(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )

            if (rationalRequired) {
                Toast.makeText(
                    context,
                    "Location permission is required for this feature to work",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Location permission is required. Please enable it from the system settings",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    if (locationUtils.hasLocationPermission(context)) {
        locationUtils.requestLocationUpdates(locationViewModel)
    } else {
        // Launch the permission request only if not already granted
        SideEffect {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}