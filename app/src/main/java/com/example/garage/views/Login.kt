package com.example.garage.views

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.garage.R
import com.example.garage.models.LocationUtils
import com.example.garage.models.ResponseObject
import com.example.garage.repository.Screen
import com.example.garage.viewModels.LocationViewModel
import com.example.garage.viewModels.LoginShearedViewModel
import com.example.garage.viewModels.MainViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException


@Composable
fun LoginScreen(
    navController: NavHostController,
    loginShearedViewModel: LoginShearedViewModel,
    locationUtils: LocationUtils,
    locationViewModel: LocationViewModel
) {

    val viewModel = viewModel<MainViewModel>()
    val context = LocalContext.current



    Scaffold(
        topBar = {
            AuthHeader()
        }
    ){
        Column(
            defaultBackground.padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            TrackLocation(
                locationUtils = locationUtils,
                locationViewModel = locationViewModel,
                context = context,

            )

            Column(
                cardModifier,
                verticalArrangement = Arrangement.Center,
            ) {
                Column {

                    Box(
                        modifier = Modifier.fillMaxWidth(1f).align(Alignment.CenterHorizontally),
                        contentAlignment = Alignment.Center,

                    ){
                        Text(
                            text = "log_in_to_road_rescue",
                            modifier = Modifier,
                            style = textStyle1,
                            fontSize = 26.sp
                        )
                    }


                    Spacer(modifier = Modifier.height(12.dp))

                    LoginBox(
                        navController = navController,
                        viewModel = viewModel,
                        loginShearedViewModel,
                        locationUtils,
                        locationViewModel
                    )
                }
            }
        }
    }


}


@Composable
fun LoginBox(
    navController: NavHostController,
    viewModel: MainViewModel,
    loginShearedViewModel: LoginShearedViewModel,
    locationUtils: LocationUtils,
    locationViewModel: LocationViewModel
) {
    val showDialog = remember { mutableStateOf(false) }
    var showLocationDialog = remember { mutableStateOf(false) }
    var processingBarStatus = remember { mutableStateOf(false) }

    var status by remember { mutableStateOf(0) }
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var buttonOneName by remember { mutableStateOf("") }
    var buttonTwoName by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    var phoneNumber by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var txtOtp by remember { mutableStateOf("") }
    val context = LocalContext.current
    var id by remember { mutableStateOf("") }

    CircularProcessingBar(isShow = processingBarStatus.value)


    Card(
        modifier = cardModifier,
        border = BorderStroke(width = 2.dp, Color.White),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFB6C7E3))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {


            Spacer(modifier = Modifier.height(60.dp))
            phoneNumber = AuthField("Registered Phone Number", "",isMobile = true)

            AuthFieldBtn(
                onClickButton = {
                    processingBarStatus.value=true

                    coroutineScope.launch {
                        try {
                            if (phoneNumber.isNotEmpty() && phoneNumber.length == 12) {
                                viewModel.checkPhoneNumberIsExists(phoneNumber, "loginSearch") { responseObject ->

                                    if (responseObject != null) {
                                        if (responseObject.status == 200) {
                                            otp= responseObject.message.toString().split(" ").lastOrNull().toString()
                                            id=responseObject.data.toString()
                                            processingBarStatus.value=false
                                            title = "OTP"
                                            message = responseObject.message.toString()
                                            buttonOneName = "null"
                                            buttonTwoName = "null"

                                            showDialog.value = true
                                        } else if(responseObject.status == 204){
                                            title = "Does not exits."
                                            message = responseObject.message.toString()
                                            buttonOneName = "null"
                                            buttonTwoName = "null"
                                            processingBarStatus.value=false
                                            showDialog.value = true
                                        } else if (responseObject.status == 500) {

                                            title = "Failed"
                                            message = responseObject.message.toString()
                                            buttonOneName = "null"
                                            buttonTwoName = "null"
                                            processingBarStatus.value=false
                                            showDialog.value = true
                                        } else {
                                            title = "Phone Number is not exists."
                                            message = responseObject.toString()
                                            buttonOneName = "null"
                                            buttonTwoName = "null"
                                            processingBarStatus.value=false
                                            showDialog.value = true
                                        }
                                    }
                                }
                            } else {
                                title = "Error..!"
                                message =
                                    "Phone number length does not match the required length. Please enter a valid phone number."
                                buttonOneName = "null"
                                buttonTwoName = "null"
                                processingBarStatus.value=false
                                showDialog.value = true
                            }
                        } catch (e: Exception) {
                            message = e.message.toString()
                            buttonOneName = "null"
                            buttonTwoName = "null"
                            processingBarStatus.value=false
                            showDialog.value = true
                        } catch (e: SocketTimeoutException) {
                            message = e.message.toString()
                            buttonOneName = "null"
                            buttonTwoName = "null"
                            processingBarStatus.value=false
                            showDialog.value = true
                        }

                    }
                }
            )
            txtOtp = AuthField("Enter the OTP", "",isMobile = false)
            //Edit button
            AuthCommonButton(
                btnName = "Log in",
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(10.dp)
            ) {

                processingBarStatus.value=true

                coroutineScope.launch {
                    if (txtOtp.isEmpty() || phoneNumber.isEmpty()){
                        processingBarStatus.value=false
                        Toast.makeText(
                            context,
                            "Enter OTP",
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{
                        if (txtOtp == otp){
                            val currentLocation =
                                locationViewModel.location.value?.latitude?.let { it1 ->
                                    locationViewModel.location.value?.longitude?.let { it2 ->
                                        LatLng(
                                            it1, it2
                                        )
                                    }
                                }

                            val part= id.split("-")

                            if (currentLocation!=null){
                                if (part[0]=="sp"){
                                    loginShearedViewModel.specificLoginId(part[1])
                                    processingBarStatus.value=false
                                    navController.navigate(Screen.GarageDashboard.route)

                                    /*val response=updateLocation(
                                        viewModel,
                                        locationViewModel.location.value!!.latitude,
                                        locationViewModel.location.value!!.longitude,
                                        "sp",
                                        part[1]
                                    )

                                    if (response != null) {
                                        if (response.status == 200) {
                                            loginShearedViewModel.specificLoginId(part[1])
                                            processingBarStatus.value=false
                                            navController.navigate(Screen.GarageDashboard.route)
                                        } else if(response.status == 204){
                                            processingBarStatus.value=false
                                            Toast.makeText(
                                                context,
                                                response.message,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else if (response.status == 400) {
                                            processingBarStatus.value=false
                                            Toast.makeText(
                                                context,
                                                response.message,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else if (response.status == 404) {
                                            processingBarStatus.value=false
                                            Toast.makeText(
                                                context,
                                                response.message,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else if (response.status == 500) {
                                            processingBarStatus.value=false
                                            Toast.makeText(
                                                context,
                                                response.message,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else if (response.status == 508) {
                                            processingBarStatus.value=false
                                            Toast.makeText(
                                                context,
                                                response.message,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            processingBarStatus.value=false
                                            Toast.makeText(
                                                context,
                                                response.message,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } else {
                                        processingBarStatus.value=false
                                        Toast.makeText(
                                            context,
                                            "Cannot call the sever",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }*/

                                }else if (part[0]=="mp"){

                                    updateLocation(
                                        viewModel,
                                        locationViewModel.location.value!!.latitude,
                                        locationViewModel.location.value!!.longitude,
                                        "mp",
                                        part[1]
                                    )

//                                    loginShearedViewModel.specificLoginId(part[1])
//                                     navController.navigate(Screen.)
                                }else if (part[0]=="t"){
                                    val response=updateLocation(
                                        viewModel,
                                        locationViewModel.location.value!!.latitude,
                                        locationViewModel.location.value!!.longitude,
                                        "t",
                                        part[1]
                                    )

                                    if (response != null) {
                                        if (response.status == 200) {
                                            loginShearedViewModel.specificLoginId(part[1])
                                            processingBarStatus.value=false
                                            navController.navigate(Screen.TechnicianDashboard.route)

                                        } else if(response.status == 204){
                                            processingBarStatus.value=false
                                            Toast.makeText(
                                                context,
                                                response.message,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else if (response.status == 400) {
                                            processingBarStatus.value=false
                                            Toast.makeText(
                                                context,
                                                response.message,
                                                Toast.LENGTH_SHORT
                                            ).show()

                                        } else if (response.status == 404) {
                                            processingBarStatus.value=false
                                            Toast.makeText(
                                                context,
                                                response.message,
                                                Toast.LENGTH_SHORT
                                            ).show()

                                        } else if (response.status == 500) {
                                            processingBarStatus.value=false
                                            Toast.makeText(
                                                context,
                                                response.message,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else if (response.status == 508) {
                                            processingBarStatus.value=false
                                            Toast.makeText(
                                                context,
                                                response.message,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            processingBarStatus.value=false
                                            Toast.makeText(
                                                context,
                                                response.message,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } else {
                                        processingBarStatus.value=false
                                        Toast.makeText(
                                            context,
                                            "Cannot call the sever",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }else{
                                    processingBarStatus.value=false
                                    Toast.makeText(
                                        context,
                                        "Error matching",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }else{
                                processingBarStatus.value=false
                                message = "Please turn on device location,with uses RoadRescue service"
                                buttonOneName = "OK"
                                showLocationDialog.value= true
                            }
                        }else{
                            processingBarStatus.value=false
                            Toast.makeText(
                                context,
                                "OTP is incorrect.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
            Spacer(
                modifier = Modifier
                    .height(2.dp)
                    .fillMaxWidth()
                    .background(Color.White)
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Haven’t registered yet?",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = textStyle2
            )
            AuthCommonButton(
                btnName = "Register",
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(10.dp)
            ) {
                navController.navigate(Screen.Register.route)
            }
            Spacer(modifier = Modifier.height(8.dp))

        }
    }

    if (showDialog.value) {
        sweetAlertDialog(
            title = title,
            message = message,
            buttonOneName = buttonOneName,
            buttonTwoName = buttonTwoName,
            onConfirm = {
                showDialog.value = false
            }
        )
    }

    if (showLocationDialog.value){
        locationAlert(
            message = message,
            buttonOneName = buttonOneName,
            onConfirm = {
                enableLocation(context)
                showLocationDialog.value=false
            }
        )
    }


}

suspend fun updateLocation(viewModel: MainViewModel, latitude: Double, longitude: Double, character:String, id:String):ResponseObject? {
    var response: ResponseObject? = null
    try {
        viewModel.updateLocation(latitude, longitude,character,id) { responseObject ->
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


@Composable
fun locationAlert(
    message: String?,
    buttonOneName: String?,
    onConfirm: () -> Unit,
) {

    AlertDialog(
        onDismissRequest = { /* Nothing to do here */ },
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "location icon",
                    modifier = deleteIconStyles.size(64.dp),
                    tint = Color(0xB5D32222)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                if (message != null) {
                    Text(
                        text = message,
                        style = textStyle2,
                        color = Color.Black,
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        },
        confirmButton = {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                Button(
                    modifier = Modifier.width(80.dp),
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(Color(0xFF253555)),
                    border = BorderStroke(width = 2.dp,color= Color.White)
                ) {

                    if (buttonOneName != null) {
                        Text(text = buttonOneName, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        containerColor = Color(0xC1CCC0C0)
    )
}

private fun enableLocation(context: Context) {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
    if (locationManager != null && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        // If location is not enabled, show location settings
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthHeader() {
    CenterAlignedTopAppBar(
        title = {
            Icon(
                painter = painterResource(id = R.drawable.logo),
                modifier = Modifier.size(100.dp),
                tint = Color.Unspecified,
                contentDescription = "Toolbar icon"
            )
        },
        navigationIcon = {
        },
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF253555)
        ),
    )
}