package com.example.garage.views.TechnicianApp

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.garage.models.ResponseObject
import com.example.garage.repository.Screen
import com.example.garage.viewModels.LoginShearedViewModel
import com.example.garage.viewModels.MainViewModel
import com.example.garage.views.AuthCommonButton
import com.example.garage.views.AuthField
import com.example.garage.views.AuthFieldBtn
import com.example.garage.views.CircularProcessingBar
import com.example.garage.views.sweetAlertDialog
import com.example.garage.views.textStyle2
import com.example.garage.views.textStyle4
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

@Composable
fun ChangeTechnicianPhoneNumWindow(
    navController: NavController,
    loginShearedViewModel: LoginShearedViewModel,
    onDismiss: () -> Unit) {

    val showDialog = remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var buttonOneName by remember { mutableStateOf("") }
    var buttonTwoName by remember { mutableStateOf("") }

    var newPhoneNumber by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    val context = LocalContext.current
    val viewModel:MainViewModel= viewModel()
    val containScope = rememberCoroutineScope()
    var processingBarStatus = remember { mutableStateOf(false) }
    var generatedOTP = "0"

    CircularProcessingBar(isShow = processingBarStatus.value)
    AlertDialog(
        onDismissRequest = { onDismiss() },
        tonalElevation = 16.dp,
        modifier = Modifier
            .padding(8.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color(0xFFDCE4EC)
            )
            .verticalScroll(rememberScrollState()),
        containerColor = Color(0xFFDCE4EC),
        confirmButton = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "Change registered phone number",
                    style = textStyle2
                )
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "(You will be logged out)",
                    style = textStyle4
                )
                Spacer(modifier = Modifier.height(16.dp))

                newPhoneNumber = AuthField("New Phone Number", "",isMobile = true)

                Spacer(modifier = Modifier.height(16.dp))

                AuthFieldBtn(
                    onClickButton = {

                        containScope.launch {

                            if (newPhoneNumber.length==12 && newPhoneNumber.isNotEmpty()){
                                val response=sentOtp(viewModel,newPhoneNumber)

                                if (response != null) {
                                    if (response.status == 200) {
                                        processingBarStatus.value=false
                                        generatedOTP=response.data.toString()
                                        Toast.makeText(
                                            context,
                                            response.data.toString(),
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
                                Toast.makeText(
                                    context,
                                    "Phone number wrong",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }

                    }
                )

                otp = AuthField("Enter the OTP", "", false)

                Spacer(modifier = Modifier.height(16.dp))

                AuthCommonButton(
                    btnName = "Confirm",
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(10.dp)
                ) {

                    containScope.launch {
                        if (otp==generatedOTP) {
                            Log.d("id", "ChangePhoneNumWindow: ${loginShearedViewModel.loginId}")
                           val response = loginShearedViewModel.loginId?.let { savePhoneNumber(it,newPhoneNumber,viewModel) }

                            if (response != null) {
                                if (response.status == 200) {
                                    processingBarStatus.value=false
                                    title = "Updated successfully"
                                    message = response.message.toString()
                                    buttonOneName = "null"
                                    buttonTwoName = "null"
                                    processingBarStatus.value=false
                                    showDialog.value = true

                                } else if (response.status == 204){
                                    Toast.makeText(
                                        context,
                                        response.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }else if (response.status == 400) {
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
                            Toast.makeText(
                                context,
                                "Incorrect OTP",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }


                }

                Spacer(modifier = Modifier.height(8.dp))
            }
            if (showDialog.value) {
                sweetAlertDialog(
                    title = title,
                    message = message,
                    buttonOneName = buttonOneName,
                    buttonTwoName = buttonTwoName,
                    onConfirm = {
                        showDialog.value = false
                        navController.navigate(Screen.SettingsScreen.route)
                    }
                )
            }
        }
    )
}

suspend fun savePhoneNumber(loginId: String, newPhoneNumber: String, viewModel: MainViewModel):ResponseObject? {
    var response: ResponseObject? = null
    try {
        viewModel.chanePhoneNumber(loginId,newPhoneNumber, "changeTechnicianPhoneNumber") { responseObject ->
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
    Log.d("TAG null", "savePhoneNumber: 9")
    return response
}

suspend fun sentOtp(viewModel: MainViewModel, newPhoneNumber: String):ResponseObject? {
    var response: ResponseObject? = null

    try {
        viewModel.checkPhoneNumberIsExists(newPhoneNumber, "sentOtp") { responseObject ->
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
