package com.example.garage.views

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.garage.R
import com.example.garage.models.LocationUtils
import com.example.garage.models.RegisterModel
import com.example.garage.models.ResponseObject
import com.example.garage.repository.Screen
import com.example.garage.viewModels.LocationViewModel
import com.example.garage.viewModels.LoginShearedViewModel
import com.example.garage.viewModels.MainViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException


@Composable
fun RegisterScreen(
    navHostController: NavHostController,
    locationUtils: LocationUtils,
    locationViewModel: LocationViewModel,
    loginShearedViewModel: LoginShearedViewModel
) {

    Scaffold(
        topBar = {
            AuthHeader()
        }
    ){
        Column(
            backgroundModifier.padding(it)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {

            Column(
                backgroundModifier,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.register_in_to_road_rescue),
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        style = textStyle1
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    SignUpBox(navHostController,locationUtils,locationViewModel,loginShearedViewModel)
                }
            }

            Footer(navController = navHostController, navStatus = "")
        }
    }



}

@Composable
fun SignUpBox(
    navController: NavHostController,
    locationUtils: LocationUtils,
    locationViewModel: LocationViewModel,
    loginShearedViewModel: LoginShearedViewModel
) {
    val showDialog = remember { mutableStateOf(false) }
    var showLocationDialog = remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var buttonOneName by remember { mutableStateOf("") }
    var buttonTwoName by remember { mutableStateOf("") }

    var viewModel:MainViewModel= viewModel()
    var ownername by remember { mutableStateOf("") }
    var garageName by remember { mutableStateOf("") }
    var serviceCategoryOption by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var txtOtp by remember { mutableStateOf("") }
    val context = LocalContext.current

    var processingBarStatus = remember { mutableStateOf(false) }
    val coroutineScope= rememberCoroutineScope()



    CircularProcessingBar(isShow = processingBarStatus.value)

    TrackLocation(
        locationUtils = locationUtils,
        locationViewModel = locationViewModel,
        context = context
    )

    Card(
        modifier = cardModifier,
        border = BorderStroke(width = 2.dp, Color.White),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFB6C7E3))// Apply shadow to the outer Box
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {


            Spacer(modifier = Modifier.height(8.dp))

            ownername = AuthField("Owner's Name", "", false, KeyboardType.Text)

            garageName = AuthField("Garage Name", "", false, KeyboardType.Text)

            phoneNumber = AuthField("A Valid Phone Number", "", true,KeyboardType.Phone)

            AuthFieldBtn {

                coroutineScope.launch {
                    processingBarStatus.value=true
                    try {
                        if (phoneNumber.isNotEmpty() && ownername.isNotEmpty() && garageName.isNotEmpty() && phoneNumber.length == 12 ) {
                            viewModel.checkPhoneNumberIsExists(phoneNumber,"registerSearch"){responseObject ->
                                if (responseObject != null) {
                                    if (responseObject.status == 200) {

                                        processingBarStatus.value=false

                                        Toast.makeText(
                                            context,
                                            responseObject.message.toString(),
                                            Toast.LENGTH_SHORT
                                        ).show()


                                    } else if(responseObject.status == 204){

                                        otp= responseObject.message.toString().split(" ").lastOrNull().toString()
                                        processingBarStatus.value=false
                                        Log.d("otp", "SignUpBox: ($otp)")

                                        Toast.makeText(
                                            context,
                                            responseObject.message,
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    } else if (responseObject.status == 500) {
                                        processingBarStatus.value=false
                                        Toast.makeText(
                                            context,
                                            responseObject.message.toString(),
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    } else {
                                        processingBarStatus.value=false
                                        Toast.makeText(
                                            context,
                                            responseObject.message.toString(),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }else {
                            processingBarStatus.value=false
                            title = "Error..!"
                            message =
                                "Phone number length does not match the required length. Please enter a valid phone number."
                            buttonOneName = "null"
                            buttonTwoName = "null"
                            showDialog.value = true
                        }
                    }catch (e: Exception) {
                        processingBarStatus.value=false
                        Toast.makeText(
                            context,
                            e.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()

                    } catch (e: SocketTimeoutException) {
                        processingBarStatus.value=false
                        Toast.makeText(
                            context,
                            e.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            }
        }

        txtOtp = AuthField("Enter the OTP", "", false, KeyboardType.Number)
        AuthCommonButton(
            btnName = "Register",
            Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp)
        ) {

            processingBarStatus.value=true

            if(phoneNumber.isNotEmpty() && txtOtp.isNotEmpty() && ownername.isNotEmpty() && garageName.isNotEmpty()){

                coroutineScope.launch {
                    if (txtOtp == otp) {
                        val currentLocation =
                            locationViewModel.location.value?.latitude?.let { it1 ->
                                locationViewModel.location.value?.longitude?.let { it2 ->
                                    LatLng(
                                        it1, it2
                                    )
                                }
                            }

                        if (currentLocation!=null){

                            val registerModel=RegisterModel(ownername,garageName,phoneNumber,locationViewModel.location.value!!.latitude,
                                locationViewModel.location.value!!.longitude,)
                             Log.d(TAG, "SignUpBox: 1")
                            val response= registerUser(viewModel,registerModel)
                            Log.d(TAG, "SignUpBox: 0")
                            if (response != null) {
                                if (response.status == 200) {
                                    loginShearedViewModel.specificLoginId(response.data.toString())
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
                            }
                        }else{
                            processingBarStatus.value=false
                            message = "Please turn on device location,with uses RoadRescue service"
                            buttonOneName = "OK"
                            showLocationDialog.value= true
                        }
                    }else{
                        Log.d("otp", "SignUpBox: $otp")
                        Log.d("otp", "SignUpBox: $txtOtp")
                        processingBarStatus.value=false
                        Toast.makeText(
                            context,
                            "OTP is incorrect.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }else{
                processingBarStatus.value=false
                Toast.makeText(
                    context,
                    "Empty fields",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        Spacer(modifier = Modifier.height(8.dp))
        Spacer(
            modifier = Modifier
                .height(2.dp)
                .fillMaxWidth()
                .background(Color.White)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Already registered?",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = textStyle2
        )
        AuthCommonButton(
            btnName = "Log in",
            Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp)
        ) {
            navController.navigate(Screen.Login.route)
        }
        Spacer(modifier = Modifier.height(8.dp))


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
}

suspend fun registerUser(viewModel: MainViewModel, registerModel: RegisterModel):ResponseObject? {
    Log.d(TAG, "SignUpBox: 2")
    var response: ResponseObject? = null
    try {
        Log.d(TAG, "SignUpBox:3")
        viewModel.registerUser(registerModel,"regUser") { responseObject ->
            Log.d(TAG, "SignUpBox: 3.1")
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
fun AuthField(labelName: String, value: String?, isMobile: Boolean,keyboardType: KeyboardType): String {
    var fieldValue by remember { mutableStateOf(TextFieldValue(value ?: "")) }

    Box(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = fieldValue,
                onValueChange = { fieldValue = it },
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                modifier = Modifier
                    .padding(horizontal = 36.dp)
                    .height(50.dp)
                    .border(2.dp, Color.White, shape = RoundedCornerShape(50))
                    .shadow(6.dp, shape = RoundedCornerShape(50))
                    .background(Color.White)
                    .align(Alignment.CenterHorizontally)
                    .onFocusChanged {
                        if (isMobile) {
                            if (it.isFocused) {
                                if (fieldValue.text.isEmpty()) {
                                    fieldValue = TextFieldValue("+94", selection = TextRange(3))
                                }
                            } else {
                                // not focused
                            }
                        }
                    },
                textStyle = TextStyle(
                    Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize =16.sp,
                    letterSpacing = 0.15.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = fontFamily
                ),
                placeholder = {
                    Text(
                        text = labelName,
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                singleLine = true
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
    return fieldValue.text
}


@Composable
fun dropDown(optionList:List<Any>,defaultSelection:Any):String{
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(defaultSelection) }

    Box(
        modifier = Modifier
            .padding(45.dp, 0.dp, 30.dp, 0.dp)
            .height(50.dp)
            .border(2.dp, Color.White, shape = RoundedCornerShape(50))
            .background(Color.White, shape = RoundedCornerShape(20.dp))
            .clickable { expanded = true },
        contentAlignment = Alignment.Center
    ){

        Row(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .clickable { expanded = true },
            horizontalArrangement = Arrangement.SpaceAround,

            ) {


            Text("$selectedOption", style = textStyle2)

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color(0xFF253555),
            )

        }


        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .fillMaxHeight(0.15f)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White,)
                .border(BorderStroke(1.dp, Color.White))
                .align(Alignment.CenterStart)

        ) {

            optionList.forEach{option ->

                DropdownMenuItem(
                    onClick = {
                        selectedOption = option
                        expanded = false
                    },
                    text = { Text(text = "$option", style = textStyle2) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

    }
    return selectedOption.toString()

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
