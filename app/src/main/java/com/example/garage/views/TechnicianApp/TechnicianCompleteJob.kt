package com.example.garage.views.TechnicianApp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.garage.R
import com.example.garage.models.LocationUtils
import com.example.garage.models.ResponseObject
import com.example.garage.models.TechnicianDashboard
import com.example.garage.repository.Screen
import com.example.garage.viewModels.LocationViewModel
import com.example.garage.viewModels.LoginShearedViewModel
import com.example.garage.viewModels.MainViewModel
import com.example.garage.viewModels.TechnicianShearedViewModel
import com.example.garage.views.CircularProcessingBar
import com.example.garage.views.CommonButton
import com.example.garage.views.CommonTextField
import com.example.garage.views.Header
import com.example.garage.views.TrackLocation
import com.example.garage.views.defaultBackground
import com.example.garage.views.textStyle4
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.net.SocketTimeoutException

@OptIn(DelicateCoroutinesApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TechnicianCompleteJob(
    navController: NavController,
    navStatus: String,
    locationUtils: LocationUtils,
    locationViewModel: LocationViewModel,
    context: Context,
    technicianShearedViewModel: TechnicianShearedViewModel,
    loginShearedViewModel: LoginShearedViewModel
) {

    var viewModel: MainViewModel = viewModel()
    var showDialog by remember { mutableStateOf(false) }
    var showPaidDialog by remember { mutableStateOf(false) }
    var showPendingDialog by remember { mutableStateOf(false) }
    var processingBarStatus = remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var service = technicianShearedViewModel.technician
    var amount by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    val mapReady = remember { mutableStateOf(false) }


    val technicianService = service?.serviceId?.let {
        TechnicianDashboard(
            it,
            service.time,
            service.description,
            service.issueCategory,
            service.customerName,
            service.customerContact,
            service.vehicleModel,
            service.customerLocation
        )
    }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                content = {
                    TechnicianSliderContent(navController,loginShearedViewModel) {
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
                TechnicianFooter(navController, navStatus)
            }
        ) {

            CircularProcessingBar(isShow = processingBarStatus.value)

            TrackLocation(
                locationUtils = locationUtils,
                locationViewModel = locationViewModel,
                context = context
            )

            Column(
                modifier = defaultBackground.padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                CommonButton(
                    btnName = "Complete Job",
                    modifier = Modifier.width(150.dp),
                    onClickButton = {
                        showDialog = true
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.84f)
                        .fillMaxHeight(0.97f)
                        .verticalScroll(state = rememberScrollState()),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFB6C7E3)),
                    border = BorderStroke(width = 2.dp, Color.White),

                    ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    service?.techId?.let { it1 ->
                        if (technicianService != null) {
                            ServiceRequest(
                                it1,
                                technicianService,
                                navController = navController,
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                context = context,
                                technicianShearedViewModel,
                                false,
                                customerLocation = service.customerLocation
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .height(340.dp)
                            .align(Alignment.CenterHorizontally),
                        shape = RoundedCornerShape(20.dp)
                    ) {

                        val currentLocation =
                            locationViewModel.location.value?.latitude?.let { it1 ->
                                locationViewModel.location.value?.longitude?.let { it2 ->
                                    LatLng(
                                        it1, it2
                                    )
                                }
                            }

                        val customerLocation = service?.customerLocation


                        val customerLat = extractCustomerLatitude(customerLocation.toString())
                        val customerLong = extractCustomerLongitude(customerLocation.toString())

                        var customerLatLong = LatLng(0.00, 0.00)

                        if(customerLat != null && customerLong != null) {
                            customerLatLong = LatLng(customerLat.toDouble(), customerLong)
                            Log.d("Custommer location", "TechnicianCompleteJob: $currentLocation")
                        }
                        Log.d("Custommer location 2", "TechnicianCompleteJob: $customerLocation")

                        val uiSettings = remember {
                            MapUiSettings(myLocationButtonEnabled = true)
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

                            // load google map

                            GoogleMap(
                                modifier = Modifier.fillMaxSize(),
                                cameraPositionState = cameraPositionState,
                                uiSettings = uiSettings,
                                onMapLoaded = { mapReady.value = true }
                            ) {
                                if(mapReady.value) {
                                    val customerIconBitmap = BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.customer)
                                    val resizedCustomerIcon = resizeBitmap(customerIconBitmap, 90, 90)
                                    val customerIcon = BitmapDescriptorFactory.fromBitmap(resizedCustomerIcon)

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

            if (showDialog) {
                Dialog(
                    onDismissRequest = { showDialog = false },
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .fillMaxHeight(0.4f)
                                .background(
                                    Color(0xFFB0B5BD),
                                    shape = RoundedCornerShape(20.dp)
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Spacer(modifier = Modifier.height(32.dp))

                            Text(
                                text = "Service Fee",
                                style = textStyle4,
                                modifier = Modifier,
                                textAlign = TextAlign.Center,
                                fontSize = 24.sp
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            amount = CommonTextField(
                                value = amount,
                                isEditing = true,
                                placeholderName = "LKR 00.00",
                                modifier = Modifier
                                    .fillMaxHeight(0.4f)
                                    .fillMaxWidth(0.7f),
                                prefixStatus = false,
                                keyboardType = KeyboardType.Number
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            CommonButton(
                                btnName = "Ask for the payment",
                                modifier = Modifier.width(200.dp)
                            ) {
                                processingBarStatus.value = true
                                coroutineScope.launch {

                                    if (amount.isNotEmpty()) {
                                        if (technicianService != null) {
                                            val response = completeJob(
                                                viewModel,
                                                technicianService.getServiceId(),
                                                amount
                                            )

                                            if (response != null) {
                                                if (response.status == 200) {
                                                    Toast.makeText(
                                                        context,
                                                        response.message,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    processingBarStatus.value = false
                                                    showDialog = false
                                                    showPaidDialog = true
                                                    // load cash or card seen eka
                                                } else if (response.status == 204) {
                                                    processingBarStatus.value = false
                                                    Toast.makeText(
                                                        context,
                                                        response.message,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else if (response.status == 400) {
                                                    processingBarStatus.value = false
                                                    Toast.makeText(
                                                        context,
                                                        response.message,
                                                        Toast.LENGTH_SHORT
                                                    ).show()

                                                } else if (response.status == 404) {
                                                    processingBarStatus.value = false
                                                    Toast.makeText(
                                                        context,
                                                        response.message,
                                                        Toast.LENGTH_SHORT
                                                    ).show()

                                                } else if (response.status == 500) {
                                                    processingBarStatus.value = false
                                                    Toast.makeText(
                                                        context,
                                                        response.message,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else if (response.status == 508) {
                                                    processingBarStatus.value = false
                                                    Toast.makeText(
                                                        context,
                                                        response.message,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    processingBarStatus.value = false
                                                    Toast.makeText(
                                                        context,
                                                        response.message,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            } else {
                                                processingBarStatus.value = false
                                                Toast.makeText(
                                                    context,
                                                    "Cannot call the sever",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    } else {
                                        processingBarStatus.value = false
                                        Toast.makeText(
                                            context,
                                            "Please enter valid amount.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                )
            }

            if (showPaidDialog) {
                Dialog(
                    onDismissRequest = { },
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .fillMaxHeight(0.3f)
                                .background(
                                    Color(0xFFB0B5BD),
                                    shape = RoundedCornerShape(20.dp)
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Spacer(modifier = Modifier.height(32.dp))

                            Text(
                                text = "Did you get your payment ?",
                                style = textStyle4,
                                modifier = Modifier,
                                textAlign = TextAlign.Center,
                                fontSize = 24.sp
                            )
                            Spacer(modifier = Modifier.height(32.dp))

                            CommonButton(btnName = "Yes", modifier = Modifier.width(150.dp)) {

                                navController.navigate(Screen.TechnicianDashboard.route)
                            }

                            Spacer(modifier = Modifier.height(8.dp))


                            CommonButton(
                                btnName = "Check if paid by card.",
                                modifier = Modifier.width(230.dp)
                            ) {
                                processingBarStatus.value = true
                                coroutineScope.launch {
                                    val result = withTimeoutOrNull(20000) {  // 20000 milliseconds = 20 seconds
                                        while (true) {
                                            val response = service?.serviceId?.let { serviceId ->
                                                checkForCustomerPaid(viewModel, serviceId, "checkPayment")
                                            }

                                            if (response != null) {

                                                if (response.status == 200) {
                                                    processingBarStatus.value = false
                                                    Toast.makeText(
                                                        context,
                                                        response.message,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    navController.navigate(Screen.TechnicianDashboard.route)
                                                } else if (response.status == 204) {
                                                    processingBarStatus.value = false
                                                    Toast.makeText(
                                                        context,
                                                        response.message,
                                                        Toast.LENGTH_SHORT
                                                    ).show()


                                                } else if (response.status == 400) {
                                                    processingBarStatus.value = false
                                                    Toast.makeText(
                                                        context,
                                                        response.message,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    Log.d("TAG-paka ai wada natte ", "TechnicianCompleteJob: ")
                                                } else if (response.status == 404) {
                                                    processingBarStatus.value = false
                                                    Toast.makeText(
                                                        context,
                                                        response.message,
                                                        Toast.LENGTH_SHORT
                                                    ).show()

                                                } else if (response.status == 500) {
                                                    processingBarStatus.value = false
                                                    Toast.makeText(
                                                        context,
                                                        response.message,
                                                        Toast.LENGTH_SHORT
                                                    ).show()

                                                } else if (response.status == 508) {
                                                    processingBarStatus.value = false
                                                    Toast.makeText(
                                                        context,
                                                        response.message,
                                                        Toast.LENGTH_SHORT
                                                    ).show()

                                                } else {
                                                    processingBarStatus.value = false
                                                    Toast.makeText(
                                                        context,
                                                        response.message,
                                                        Toast.LENGTH_SHORT
                                                    ).show()

                                                }
                                                break  // Stop the loop if a response is received
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Cannot call the server",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                break  // Stop the loop on failure to receive a response
                                            }
                                            delay(10000)  // Wait for 10 seconds before checking again
                                        }
                                    }
                                    if (result == null) {
                                        // This block runs only if the timeout was reached without breaking the loop
                                        processingBarStatus.value = false
                                        Toast.makeText(
                                            context,
                                            "Checking for payment timed out",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                )
            }

            /*if (showPendingDialog){
                Dialog(
                    onDismissRequest = { },
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .fillMaxHeight(0.3f)
                                .background(
                                    Color(0xFFB0B5BD),
                                    shape = RoundedCornerShape(20.dp)
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                        }
                    }
                )
            }*/
        }
    }
}

fun extractCustomerLatitude(input: String): Double? {
    val pattern = "latitude=([\\d.+-]+)".toRegex()
    val matchResult = pattern.find(input)
    return matchResult?.groupValues?.get(1)?.toDouble()
}

fun extractCustomerLongitude(input: String): Double? {
    val pattern = "longitude=([\\d.+-]+)".toRegex()
    val matchResult = pattern.find(input)
    return matchResult?.groupValues?.get(1)?.toDouble()
}

suspend fun checkForCustomerPaid(
    viewModel: MainViewModel,
    serviceId: String,
    option: String,
): ResponseObject? {
    var response: ResponseObject? = null

    try {
        viewModel.checkPayment(serviceId, option) { responseObject ->
            if (responseObject != null) {
                response = responseObject
            } else {
                response = ResponseObject(400, "response is null", null)
            }
        }
    } catch (e: SocketTimeoutException) {
        response = ResponseObject(508, "Request time out.\n Please try again.", e.localizedMessage)
    } catch (e: Exception) {
        response = ResponseObject(404, "Exception error.", e.localizedMessage)
    }
    return response
}

suspend fun completeJob(
    viewModel: MainViewModel,
    serviceId: String,
    amount: String,
): ResponseObject? {
    var response: ResponseObject? = null

    try {
        viewModel.completeJob(amount, "completeJob", serviceId) { responseObject ->
            if (responseObject != null) {
                response = responseObject
            } else {
                response = ResponseObject(400, "response is null", null)
            }
        }
    } catch (e: SocketTimeoutException) {
        response = ResponseObject(508, "Request time out.\n Please try again.", e.localizedMessage)
    } catch (e: Exception) {
        response = ResponseObject(404, "Exception error.", e.localizedMessage)
    }
    return response
}

fun resizeBitmap(source: Bitmap, width: Int, height: Int): Bitmap {
    return Bitmap.createScaledBitmap(source, width, height, false)
}