package com.example.garage.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.garage.models.Garage
import com.example.garage.models.ResponseObject
import com.example.garage.models.ServicesRequestModel
import com.example.garage.repository.GarageCommonDetails
import com.example.garage.repository.Screen
import com.example.garage.viewModels.GarageSharedViewModel
import com.example.garage.viewModels.LocationViewModel
import com.example.garage.viewModels.LoginShearedViewModel
import com.example.garage.viewModels.MainViewModel
import com.example.garage.viewModels.NotificationViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.SocketTimeoutException
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GarageDashboard(
    navController: NavController,
    navStatus: String,
    garageSharedViewModel: GarageSharedViewModel,
    loginShearedViewModel: LoginShearedViewModel,
    notificationViewModel: NotificationViewModel,
    locationViewModel: LocationViewModel,
) {

    var garageId = loginShearedViewModel.loginId
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val viewModel = viewModel<MainViewModel>()

    var showLoadGarageDetails by remember { mutableStateOf(false) }
    var showServiceRequests by remember { mutableStateOf(false) }
    var showMessageDialog by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var buttonOneName by remember { mutableStateOf("") }
    var buttonTwoName by remember { mutableStateOf("") }
    var garage by remember { mutableStateOf("") }
    var requestServices by remember { mutableStateOf("") }
    var garageDetailsBackend = Garage()
    val context: Context = LocalContext.current
    var preServicesLength: Int = 0

    LaunchedEffect(Unit) {
        val response = garageId?.let { loadGarageDetails(viewModel, searchId = it) }
        if (response != null) {

            if (response.status == 200) {

                garage = response.data!!.toString()
//                showProgressBar=false
                showLoadGarageDetails = true

            } else if (response.status == 400) {
                title = response.status.toString()
                message = response.message.toString()
                buttonOneName = "Ok"
                buttonTwoName = "null"
                showMessageDialog = true

            } else if (response.status == 404) {
                title = response.status.toString()
                message = response.message.toString()
                buttonOneName = "Ok"
                buttonTwoName = "null"
                showMessageDialog = true

            } else if (response.status == 500) {
                title = response.status.toString()
                message = response.message.toString()
                buttonOneName = "Ok"
                buttonTwoName = "null"
                showMessageDialog = true
            } else if (response.status == 508) {
                title = response.status.toString()
                message = response.message.toString()
                buttonOneName = "null"
                buttonTwoName = "null"
                showMessageDialog = true
            } else {
                title = response.status.toString()
                message = response.message.toString()
                buttonOneName = "Ok"
                buttonTwoName = "null"
                showMessageDialog = true
            }
        } else {
            title = "401"
            message = "Cannot call the sever"
            buttonOneName = "Ok"
            buttonTwoName = "null"
            showMessageDialog = true
            Log.d("response null", "null")
        }


        notificationViewModel.initNotificationChannel(context = context)
        // fetch services requests every 1 minutes
        while (true) {

            val serviceResponse = fetchServiceRequests(viewModel)

            if (serviceResponse != null) {
                if (serviceResponse.status == 200) {

                    requestServices = serviceResponse.data!!.toString()
                    Log.d("service check 00", serviceResponse.data.toString())

                    if (requestServices.length > preServicesLength) {

                        preServicesLength = requestServices.length
                        requestServices = ""
                        requestServices = serviceResponse.data!!.toString()
                        //send notification
                        notificationViewModel.sendNotification(
                            context,
                            "New Service Request",
                            "You have received a new service request"
                        )
                        showServiceRequests = true
                    }
                } else if (serviceResponse.status == 400) {
                    title = serviceResponse.status.toString()
                    message = serviceResponse.message.toString()
                    buttonOneName = "Ok"
                    buttonTwoName = "null"
                    showMessageDialog = true

                } else if (serviceResponse.status == 404) {
                    title = serviceResponse.status.toString()
                    message = serviceResponse.message.toString()
                    buttonOneName = "Ok"
                    buttonTwoName = "null"
                    showMessageDialog = true

                } else if (serviceResponse.status == 500) {
                    title = serviceResponse.status.toString()
                    message = serviceResponse.message.toString()
                    buttonOneName = "Ok"
                    buttonTwoName = "null"
                    showMessageDialog = true
                } else if (serviceResponse.status == 508) {
                    title = serviceResponse.status.toString()
                    message = serviceResponse.message.toString()
                    buttonOneName = "null"
                    buttonTwoName = "null"
                    showMessageDialog = true
                }
            } else {
                title = "401"
                message = "Can not call the sever"
                buttonOneName = "Ok"
                buttonTwoName = "null"
                showMessageDialog = true
            }
            delay(1000 * 10)
        }


    }


    if (showLoadGarageDetails) {
        try {
            val jsonObject = JSONObject(garage)

            val garageName = jsonObject.getString("garageName")
            val ownerName = jsonObject.getString("OwnerName")
            val garageContactNumber = jsonObject.getString("contactNumber")
            val garageStatus = jsonObject.getString("garageStatus")
            val garageEmail = jsonObject.getString("email")
            val garageRating = jsonObject.getString("garageRating").toFloat()
            val garageType = jsonObject.getString("garageType")
            val garageProfileImageRef = jsonObject.getString("imageRef")

            garageDetailsBackend.setGarageName(garageName)
            garageDetailsBackend.setOwnerName(ownerName)
            garageDetailsBackend.setGarageContactNumber(garageContactNumber)
            garageDetailsBackend.setGarageStatus(garageStatus)
            garageDetailsBackend.setGarageEmail(garageEmail)
            garageDetailsBackend.setGarageRating(garageRating)
            garageDetailsBackend.setGarageType(garageType)
            garageDetailsBackend.setGarageProfilePicRef(garageProfileImageRef)


            // Sheared garage common details using Parcelize
            val garageCommonDetails = garageId?.let {
                GarageCommonDetails(
                    it,
                    garageDetailsBackend.getGarageName(),
                    garageDetailsBackend.getGarageContactNumber(),
                    garageDetailsBackend.getGarageStatus(),
                    garageDetailsBackend.getGarageEmail(),
                    garageDetailsBackend.getGarageRating().toString(),
                    garageDetailsBackend.getGarageType(),
                    garageDetailsBackend.getOwnerName(),
                    garageDetailsBackend.getGarageProfilePicRef()
                )
            }
            if (garageCommonDetails != null) {
                garageSharedViewModel.garageCommonDetails(garageCommonDetails)
            }


        } catch (e: JSONException) {
            e.localizedMessage?.let { it1 -> Log.d("json error", it1) }
        }

    }




    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                content = {
                    SidebarContent(navController,loginShearedViewModel) {
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
                Footer(navController, navStatus)
            }
        ) {
            Column(
                modifier = defaultBackground.padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {


                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Welcome ${garageDetailsBackend.getGarageName()}",
                    color = Color(0xFF253555),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    style = textStyle4
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.84f)
                        .fillMaxHeight(0.95f)
                        .verticalScroll(state = rememberScrollState()),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFB6C7E3)),
                    border = BorderStroke(width = 2.dp, Color.White),

                    ) {

                    Spacer(modifier = Modifier.height(16.dp))

                    // Load are service requests
                    // customer request load karanna one
                    if (showServiceRequests) {
                        if (requestServices.isNotEmpty()) {
                            Log.d("service check", requestServices)
                            val jsonArray = JSONArray(requestServices)
                            if (jsonArray.length() > preServicesLength) {

                                for (i in 0 until jsonArray.length()) {
                                    val jsonObject = jsonArray.getJSONObject(i)
                                    val issue = jsonObject.getString("issue")
                                    val customerContactNumber =
                                        jsonObject.getString("customerContactNumber")
                                    val approx_cost = jsonObject.getDouble("approx_cost")
                                    val requestTime = jsonObject.getString("requestTimeStamp")
                                    val description = jsonObject.getString("description")
                                    val indicatorLightStatus =
                                        jsonObject.getString("indicatorLightStatus")
                                    val serviceRequestId = jsonObject.getInt("serviceRequestId")
                                    val serviceLocation = jsonObject.getString("serviceLocation")
                                    //  val garageVerification = jsonObject.getString("verification")

                                    // methananwens karanna wi balanna weyi


                                    val garageLat = locationViewModel.location.value?.latitude
                                    val garageLong = locationViewModel.location.value?.longitude


                                    var serviceRequest = ServicesRequestModel(
                                        serviceRequestId,
                                        customerContactNumber,
                                        requestTime,
                                        issue,
                                        description,
                                        approx_cost,
                                        indicatorLightStatus,
                                        ""
                                    )

                                    ServiceRequest(
                                        navController,
                                        serviceRequest,
                                        Modifier.align(Alignment.CenterHorizontally),
                                        viewModel,
                                        loginShearedViewModel
                                    )


                                    /*if (garageVerification == "Yes") {



                                    } else {
                                        title = "Garage not verified"
                                        message = "Unverified garage detected. Please verify."
                                        buttonOneName = "Ok"
                                        buttonTwoName = "null"
                                        showMessageDialog = true
                                    }*/

                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }

                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

            }

            if (showMessageDialog) {
                sweetAlertDialog(
                    title = title,
                    message = message,
                    buttonOneName = buttonOneName,
                    buttonTwoName = buttonTwoName,
                    onConfirm = {
                        showMessageDialog = false
                    }
                )
            }
        }
    }
}

fun getBoundingBox(
    lat: Double,
    lon: Double,
    radiusKm: Double = 3.0,
): Pair<Pair<Double, Double>, Pair<Double, Double>> {
    val earthRadiusKm = 6371.0 // Radius of the Earth in kilometers
    val radPerDeg = PI / 180 // Radians per degree
    val distFraction =
        radiusKm / earthRadiusKm // Fraction of the Earth's radius covered by the radiusKm

    // Calculate latitude bounds
    val minLat = lat - distFraction * 180 / PI
    val maxLat = lat + distFraction * 180 / PI

    // Calculate longitude bounds. Cosine factor adjusts for convergence of longitude lines at higher latitudes.
    val minLon = lon - distFraction * 180 / PI / cos(lat * radPerDeg)
    val maxLon = lon + distFraction * 180 / PI / cos(lat * radPerDeg)

    return Pair(Pair(minLat, maxLat), Pair(minLon, maxLon))
}

suspend fun fetchServiceRequests(viewModel: MainViewModel): ResponseObject? {
    var response: ResponseObject? = null

    try {
        viewModel.getGarageServiceRequest("1", "getServices") { responseObject ->
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

suspend fun loadGarageDetails(viewModel: MainViewModel, searchId: String): ResponseObject? {
    var response: ResponseObject? = null

    try {
        viewModel.getGarageDetails(searchId, "search") { responseObject ->
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

fun isWithinRadius(
    centerLat: Double,
    centerLon: Double,
    pointLat: Double,
    pointLon: Double,
    radiusKm: Double = 3.0,
): Boolean {
    val earthRadiusKm = 6371.0 // Earth's radius in kilometers

    val dLat = (pointLat - centerLat) * PI / 180
    val dLon = (pointLon - centerLon) * PI / 180
    val a = sin(dLat / 2).pow(2) +
            cos(centerLat * PI / 180) * cos(pointLat * PI / 180) *
            sin(dLon / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    val distance = earthRadiusKm * c

    return distance <= radiusKm
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ServiceRequest(
    navController: NavController,
    serviceRequest: ServicesRequestModel,
    modifier: Modifier,
    viewModel: MainViewModel,
    loginShearedViewModel: LoginShearedViewModel
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val phoneNumber = serviceRequest.getCustomerContactNumber()
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var buttonOneName by remember { mutableStateOf("") }
    var buttonTwoName by remember { mutableStateOf("") }
    var showMessageDialog by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showAlert by remember { mutableStateOf(false) }
    var cancleDialog by remember { mutableStateOf(false) }
    var techniciansList = emptyList<String>()


    Card(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.35f)
            .shadow(elevation = 8.dp, shape = RectangleShape),
        colors = CardDefaults.cardColors(containerColor = Color.White),

        ) {


        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "You have new service request ${serviceRequest.getTime()}",
            color = Color(0xFF253555),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Divider(color = Color(0xFF253555), thickness = 2.dp)

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Issue", color = Color.Black, modifier = Modifier
                    .weight(1f)
                    .padding(8.dp, 0.dp)
            )
            Text(
                text = serviceRequest.getIssue(),
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Indicator light state", color = Color.Black, modifier = Modifier
                    .weight(1f)
                    .padding(8.dp, 0.dp)
            )
            Text(
                text = serviceRequest.getIndicatorState(),
                color = Color.Black,
                modifier = Modifier.weight(1f),
                maxLines = 3
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Description", color = Color.Black, modifier = Modifier
                    .weight(1f)
                    .padding(8.dp, 0.dp)
            )
            Text(
                text = if (serviceRequest.getDescription()
                        .isEmpty()
                ) "No Description" else serviceRequest.getDescription(),
                color = Color.Black,
                modifier = Modifier.weight(1f),
                maxLines = 3
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Service Fees(approx..)", color = Color.Black, modifier = Modifier
                    .weight(1f)
                    .padding(8.dp, 0.dp)
            )
            Text(
                text = "LKR ${serviceRequest.getServiceFee()}0",
                color = Color.Black,
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {

                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))

                context.startActivity(intent)

            }) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Contact icon",
                    tint = Color.Black
                )
            }
            
            CommonButton(
                btnName = "Cancel",
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {

                cancleDialog=true

            }
            

            CommonButton(
                btnName = "Accept",
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {

                coroutineScope.launch {
                    var technicianResponse: ResponseObject? = null
                    when (serviceRequest.getIssue()) {
                        "Mechanical Issues" -> {
                            technicianResponse = loadTechniciansGroupByIssue(viewModel, "Other")
                        }

                        "Electrical Issues" -> {
                            technicianResponse = loadTechniciansGroupByIssue(
                                viewModel,
                                "Electrical System Troubleshooting"
                            )
                        }

                        "Engine Problems" -> {
                            technicianResponse =
                                loadTechniciansGroupByIssue(viewModel, "Engine Maintenance")
                        }

                        "Fuel Issues" -> {
                            technicianResponse =
                                loadTechniciansGroupByIssue(viewModel, "Oil System Maintenance")
                        }

                        "Exhaust Issues" -> {
                            technicianResponse =
                                loadTechniciansGroupByIssue(viewModel, "Engine Maintenance")
                        }

                        "Cooling Problems" -> {
                            technicianResponse = loadTechniciansGroupByIssue(viewModel, "HVAC")
                        }

                        "Other" -> {
                            technicianResponse = loadTechniciansGroupByIssue(viewModel, "Other")
                        }
                    }

                    if (technicianResponse != null) {
                        if (technicianResponse.status == 200) {


                            val filterTechnicians = technicianResponse.data!!.toString()

                            val jsonArray = JSONArray(filterTechnicians)

                            Log.d("filter techniciand list pre", filterTechnicians)

                            for (i in 0 until jsonArray.length()) {
                                val jsonObject = jsonArray.getJSONObject(i)
                                val techId = jsonObject.getString("techId")
                                val techFName = jsonObject.getString("f_name")
                                val techLName = jsonObject.getString("l_name")

                                techniciansList += "$techId-$techFName $techLName"
                            }

                            Log.d("filter techniciand list after", techniciansList.toString())

                        } else if (technicianResponse.status == 400) {
                            title = technicianResponse.status.toString()
                            message = technicianResponse.message.toString()
                            buttonOneName = "Ok"
                            buttonTwoName = "null"
                            showMessageDialog = true

                        } else if (technicianResponse.status == 404) {
                            title = technicianResponse.status.toString()
                            message = technicianResponse.message.toString()
                            buttonOneName = "Ok"
                            buttonTwoName = "null"
                            showMessageDialog = true

                        } else if (technicianResponse.status == 500) {
                            title = technicianResponse.status.toString()
                            message = technicianResponse.message.toString()
                            buttonOneName = "Ok"
                            buttonTwoName = "null"
                            showMessageDialog = true
                        } else if (technicianResponse.status == 508) {
                            title = technicianResponse.status.toString()
                            message = technicianResponse.message.toString()
                            buttonOneName = "null"
                            buttonTwoName = "null"
                            showMessageDialog = true
                        } else {
                            title = technicianResponse.status.toString()
                            message = technicianResponse.message.toString()
                            buttonOneName = "Ok"
                            buttonTwoName = "null"
                            showMessageDialog = true
                        }
                    } else {
                        title = "401"
                        message = "Cannot call the sever"
                        buttonOneName = "Ok"
                        buttonTwoName = "null"
                        showMessageDialog = true
                    }
                }
                showDialog = true
            }

            if (showDialog) {
                Dialog(
                    onDismissRequest = { },
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .fillMaxHeight(0.4f)
                                .background(
                                    Color(0xFFACB3C0),
                                    shape = RoundedCornerShape(20.dp)
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {


                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(onClick = {
                                    techniciansList = emptyList()
                                    showDialog = false
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "close icon",
                                        modifier = closerButtonStyles,
                                        tint = Color.White
                                    )
                                }
                            }


                            Text(
                                text = "Assign a Technician ",
                                style = textStyle4,
                                modifier = Modifier,
                                textAlign = TextAlign.Center,
                                fontSize = 24.sp
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            // Dropdown load

                            Log.d("filter techniciand list", "$techniciansList")

                            val option = CommonDropdown(
                                optionList = techniciansList,
                                defaultSelection = "Technician",
                                Modifier
                            )


                            Spacer(modifier = Modifier.height(64.dp))

                            // accept button load


                            CommonButton(
                                btnName = "Assign",
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                onClickButton = {

                                    Log.d("dropDownValue", "$option")

                                    val serviceProviderId = "1"
                                    Log.d(
                                        "TAG test assign button is work",
                                        "ServiceRequest: work assign button"
                                    )
                                    coroutineScope.launch {


                                        val responseObject = loginShearedViewModel.loginId?.let {
                                            assignTechnicianForService(
                                                viewModel,
                                                serviceRequest.getServiceRequestId(),
                                                it,
                                                option.toString()
                                            )
                                        }

                                        if (responseObject != null) {
                                            if (responseObject.status == 200) {
                                                title = "Success"
                                                message = responseObject.message.toString()
                                                buttonOneName = "null"
                                                buttonTwoName = "null"
                                                showAlert = true

                                            } else if (responseObject.status == 400) {
                                                title = responseObject.status.toString()
                                                message = responseObject.message.toString()
                                                buttonOneName = "Ok"
                                                buttonTwoName = "null"
                                                showAlert = true

                                            } else if (responseObject.status == 404) {
                                                title = responseObject.status.toString()
                                                message = responseObject.message.toString()
                                                buttonOneName = "Ok"
                                                buttonTwoName = "null"
                                                showAlert = true

                                            } else if (responseObject.status == 500) {
                                                title = responseObject.status.toString()
                                                message = responseObject.message.toString()
                                                buttonOneName = "Ok"
                                                buttonTwoName = "null"
                                                showAlert = true
                                            } else if (responseObject.status == 508) {
                                                title = responseObject.status.toString()
                                                message = responseObject.message.toString()
                                                buttonOneName = "null"
                                                buttonTwoName = "null"
                                                showAlert = true
                                            } else {
                                                title = responseObject.status.toString()
                                                message = responseObject.message.toString()
                                                buttonOneName = "null"
                                                buttonTwoName = "null"
                                                showAlert = true
                                            }
                                        } else {
                                            title = "401"
                                            message = "Cannot call the sever"
                                            buttonOneName = "Ok"
                                            buttonTwoName = "null"
                                            showAlert = true
                                        }
                                    }


                                }
                            )

                        }
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }

    if (cancleDialog){
        Dialog(
            onDismissRequest = { },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.25f)
                        .background(
                            Color(0xFFACB3C0),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "delete icon",
                            modifier = deleteIconStyles.size(64.dp),
                            tint = Color(0xB5D32222),

                            )
                    }


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Do you really want to cancel this request ? ",
                            style = textStyleDefault
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        CommonButton(
                            btnName = "Cancel",
                            modifier = Modifier,
                            onClickButton = { cancleDialog = false })

                        CommonButton(btnName = "Yes", modifier = Modifier, onClickButton = {
                            // handle technician delete operation

                            cancleDialog = false

                            coroutineScope.launch {

                                val response=candleServiceRequest(viewModel,serviceRequest.getServiceRequestId())

                                if (response != null) {
                                    if (response.status == 200) {
                                        Toast.makeText(
                                            context,
                                            response.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else if (response.status == 400) {
                                        Toast.makeText(
                                            context,
                                            response.message,
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    } else if (response.status == 404) {
                                        Toast.makeText(
                                            context,
                                            response.message,
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    } else if (response.status == 500) {
                                        Toast.makeText(
                                            context,
                                            response.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else if (response.status == 508) {
                                        Toast.makeText(
                                            context,
                                            response.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Can not call server",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }
                        })

                    }


                }
            }
        )
    }


    if (showMessageDialog) {
        sweetAlertDialog(
            title = title,
            message = message,
            buttonOneName = buttonOneName,
            buttonTwoName = buttonTwoName,
            onConfirm = {
                showMessageDialog = false
            }
        )
    }

    if (showAlert) {
        sweetAlertDialog(
            title = title,
            message = message,
            buttonOneName = buttonOneName,
            buttonTwoName = buttonTwoName,
            onConfirm = {
                showMessageDialog = false
                navController.navigate(route = Screen.GarageDashboard.route)
            }
        )
    }
}

suspend fun candleServiceRequest(viewModel: MainViewModel, serviceRequestId: Int):ResponseObject? {
    var response: ResponseObject? = null

    try {
        viewModel.changeStatus(serviceRequestId.toString(),"6","candle") { responseObject ->
            if (responseObject != null) {
                response = responseObject
            } else {
                response = ResponseObject(400, "response is null", null)
            }
        }
    } catch (e: SocketTimeoutException) {
        // handle
        response = ResponseObject(508, "Request time out.\n Please try again.", e.localizedMessage)
    } catch (e: Exception) {
        response = ResponseObject(404, "Exception error.", e.localizedMessage)
    }

    return response
}


suspend fun assignTechnicianForService(
    viewModel: MainViewModel,
    serviceRequestId: Int,
    serviceProviderId: String,
    technicianId: String,
): ResponseObject? {
    var response: ResponseObject? = null

    try {
        viewModel.assignTechnicianForService(
            "assignTechnician",
            serviceRequestId,
            serviceProviderId,
            technicianId
        ) { responseObject ->
            if (responseObject != null) {
                response = responseObject
            } else {
                response = ResponseObject(400, "response is null", null)
            }
        }
    } catch (e: SocketTimeoutException) {
        // handle
        response = ResponseObject(508, "Request time out.\n Please try again.", e.localizedMessage)
    } catch (e: Exception) {
        response = ResponseObject(404, "Exception error.", e.localizedMessage)
    }

    return response
}

suspend fun loadTechniciansGroupByIssue(
    viewModel: MainViewModel,
    issueCategory: String,
): ResponseObject? {
    var response: ResponseObject? = null

    try {
        viewModel.getTechnicians("$issueCategory-1", "filterTechByIssue") { responseObject ->
            if (responseObject != null) {
                response = responseObject
            } else {
                response = ResponseObject(400, "response is null", null)
            }
        }
    } catch (e: SocketTimeoutException) {
        // handle
        response = ResponseObject(508, "Request time out.\n Please try again.", e.localizedMessage)
    } catch (e: Exception) {
        response = ResponseObject(404, "Exception error.", e.localizedMessage)
    }

    return response

}
