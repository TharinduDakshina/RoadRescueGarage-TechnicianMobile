package com.example.garage.views.TechnicianApp

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.garage.R
import com.example.garage.viewModels.LocationViewModel
import com.example.garage.viewModels.LoginShearedViewModel
import com.example.garage.viewModels.TechnicianShearedViewModel
import com.example.garage.views.Header
import com.example.garage.views.cardModifier
import com.example.garage.views.defaultBackground
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun TechnicianTrackLocationScreen(
    navController: NavController,
    loginShearedViewModel: LoginShearedViewModel,
    locationViewModel: LocationViewModel,
    technicianShearedViewModel: TechnicianShearedViewModel,
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var service = technicianShearedViewModel.technician
    val mapReady = remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                content = {
                    TechnicianSliderContent(navController, loginShearedViewModel) {
                        scope.launch {
                            drawerState.close()
                        }
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                Header {
                    scope.launch { drawerState.open() }
                }
            },
            bottomBar = {
                TechnicianFooter(navController, "")
            }
        ) {
            Column(
                defaultBackground.padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {


                Card(
                    modifier = cardModifier
                        .fillMaxHeight()
                        .align(Alignment.CenterHorizontally)
                        .verticalScroll(state = rememberScrollState()),
                    border = BorderStroke(width = 2.dp, Color.White),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFB6C7E3)),

                    ) {
                    Spacer(modifier = Modifier.height(12.dp))


                    Spacer(modifier = Modifier.height(8.dp))

                    val currentLocation =
                        locationViewModel.location.value?.latitude?.let { it1 ->
                            locationViewModel.location.value?.longitude?.let { it2 ->
                                LatLng(
                                    it1, it2
                                )
                            }
                        }


                    // load google map
                    val uiSettings = remember {
                        MapUiSettings(myLocationButtonEnabled = true)
                    }


                    val customerLocation = service?.customerLocation


                    val customerLat = extractCustomerLatitude(customerLocation.toString())
                    val customerLong = extractCustomerLongitude(customerLocation.toString())

                    var customerLatLong = LatLng(0.00, 0.00)

                    if (customerLat != null && customerLong != null) {
                        customerLatLong = LatLng(customerLat.toDouble(), customerLong)
                        Log.d("Custommer location", "TechnicianCompleteJob: $currentLocation")
                    }


                    if (currentLocation != null) {
                        val cameraPositionState = rememberCameraPositionState {
                            position =
                                currentLocation.let { it1 ->
                                    CameraPosition.fromLatLngZoom(
                                        it1,
                                        15f
                                    )
                                }
                        }



                        GoogleMap(
                            modifier = Modifier.fillMaxSize()
                                .height(580.dp).align(Alignment.CenterHorizontally)
                                .border(BorderStroke(width = 2.dp, Color.White)),
                            cameraPositionState = cameraPositionState,
                            uiSettings = uiSettings,
                            onMapLoaded = { mapReady.value = true }
                        ) {
                            if (mapReady.value) {
                                val customerIconBitmap = BitmapFactory.decodeResource(
                                    LocalContext.current.resources, R.drawable.customer
                                )
                                val resizedCustomerIcon = resizeBitmap(customerIconBitmap, 90, 90)
                                val customerIcon =
                                    BitmapDescriptorFactory.fromBitmap(resizedCustomerIcon)

                                currentLocation?.let { it1 -> MarkerState(position = it1) }
                                    ?.let { it2 ->
                                        Marker(
                                            state = it2,
                                            title = "Technician Location",
                                        )
                                        Marker(
                                            state = MarkerState(position = customerLatLong),
                                            title = "Customer location",
                                            icon = customerIcon
                                        )
                                    }
                            }
                        }

                    }
                }
            }
        }
    }
}
