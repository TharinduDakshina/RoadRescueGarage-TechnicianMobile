package com.example.garage.views.TechnicianApp

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.garage.models.ResponseObject
import com.example.garage.repository.Screen
import com.example.garage.viewModels.LoginShearedViewModel
import com.example.garage.viewModels.MainViewModel
import com.example.garage.views.SidebarButton
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

@Composable
fun TechnicianSliderContent(
    navController: NavController,
    loginShearedViewModel: LoginShearedViewModel,
    menuClicked:()->Unit
) {

    var viewModel:MainViewModel= viewModel()
    val containScope= rememberCoroutineScope()
    val context = LocalContext.current

    Column(


        modifier = Modifier

            .fillMaxHeight()

            .width(250.dp)

            .background(Color(0xFF253555))

    ) {

        Column {

            Icon(
                imageVector = Icons.Filled.Menu,
                tint = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .size(30.dp)
                    .clickable {
                        menuClicked()
                    },
                contentDescription = "Localized Description"
            )

            Spacer(modifier = Modifier
                .background(Color.White)
                .height(2.dp))
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
        ) {
            var garageOpenClosedStatus by remember { mutableStateOf("Deactivate") }

            SidebarButton(buttonName=garageOpenClosedStatus,verticalPadding=8, onClick = {

                containScope.launch {
                    garageOpenClosedStatus= loginShearedViewModel.loginId?.let {
                        chaneTechnicianState(
                            viewModel,
                            garageOpenClosedStatus,
                            it,
                            context
                        )
                    }.toString()
                }



            })

            SidebarButton(buttonName="Activities",verticalPadding=8, onClick = {
                navController.navigate(Screen.TechnicianActivities.route)
            })

            SidebarButton(buttonName="Help",verticalPadding=8, onClick = {
                navController.navigate(Screen.TechHelpScreen.route)
            })

            SidebarButton(buttonName="Settings",verticalPadding=8, onClick = {
                navController.navigate(Screen.ChangeTechnicianPhoneNumber.route)
            })

        }

        SidebarButton(buttonName="Log Out",verticalPadding=16, onClick = {
            navController.navigate(Screen.Login.route)
        })
    }

}

suspend fun chaneTechnicianState(viewModel: MainViewModel,garageOpenClosedStatus: String,technicianId: String,context: Context):String {
    var stastus=""
    if (garageOpenClosedStatus == "Deactivate") {
        val response= changeTechnicianStatus(viewModel,"1",technicianId)

        if (response != null) {
            if (response.status == 200) {

                stastus="Activate"

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


    }else{

        val response= changeTechnicianStatus(viewModel,"0",technicianId)

        if (response != null) {
            if (response.status == 200) {

                stastus="Deactivate"

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
    return stastus
}



suspend fun changeTechnicianStatus(viewModel: MainViewModel, status: String, technicianId: String): ResponseObject?{

    Log.d("TAG", "chaneTechnicianState: $technicianId")
    Log.d("TAG", "chaneTechnicianState: $status")

    var response: ResponseObject? = null

    try {
        viewModel.changeStatus(technicianId,status,"technician") { responseObject ->
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