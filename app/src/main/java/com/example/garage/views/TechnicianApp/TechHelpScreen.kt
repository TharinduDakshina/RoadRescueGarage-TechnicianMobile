package com.example.garage.views.TechnicianApp

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.garage.R
import com.example.garage.models.CustomerSupportTicket
import com.example.garage.models.IssueSupportTicket
import com.example.garage.models.ResponseObject
import com.example.garage.repository.Screen
import com.example.garage.viewModels.LoginShearedViewModel
import com.example.garage.viewModels.MainViewModel
import com.example.garage.views.CommonDropdown
import com.example.garage.views.CommonTextField
import com.example.garage.views.CustomerSupport
import com.example.garage.views.Header
import com.example.garage.views.cardModifier
import com.example.garage.views.defaultBackground
import com.example.garage.views.loadCustomerSupports
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.net.SocketTimeoutException

@Composable
fun TechHelpScreen(
    navController: NavController,
    loginShearedViewModel: LoginShearedViewModel
){
    val techId=loginShearedViewModel.loginId
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val viewModel: MainViewModel = viewModel()
    var txtMessage by remember { mutableStateOf("") }
    val issueList =
        listOf<String>("Login issue", "Payment issue", "Technical glitch", "FeedBack", "Other")

    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var buttonOneName by remember { mutableStateOf("") }
    var buttonTwoName by remember { mutableStateOf("") }
    var responseString by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    var showMessageDialog by remember { mutableStateOf(false) }
    var showCSMembers by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val response = loadCustomerSupports(viewModel)
        if (response != null) {
            if (response.status == 200) {

                responseString = response.data!!.toString()
//                showProgressBar=false
                showCSMembers = true

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
                TechnicianFooter(navController, "")
            }
        ) {
            Column(
                defaultBackground.padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
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

                        Spacer(modifier = Modifier.height(8.dp))


                        val option = CommonDropdown(
                            optionList = issueList,
                            defaultSelection = "Select Issue ",
                            Modifier
                                .fillMaxWidth(0.8f)
                                .fillMaxHeight(0.08f)
                                .padding(24.dp, 0.dp, 0.dp, 0.dp)
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .align(Alignment.CenterHorizontally),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            txtMessage = CommonTextField(
                                value = txtMessage,
                                isEditing = true,
                                placeholderName = "Write your problem ..",
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(0.1f),
                                prefixStatus = false,
                                keyboardType = KeyboardType.Text
                            )


                            IconButton(onClick = {


                                coroutineScope.launch {


                                    if (txtMessage.isEmpty()) {
                                        Toast.makeText(
                                            context,
                                            "Please enter your problem.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else if (option == "Select Issue ") {
                                        Toast.makeText(
                                            context,
                                            "Select the issue category",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else if (txtMessage.isEmpty() && option == "Select Issue "){
                                        Toast.makeText(
                                            context,
                                            "Enter your problem.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        val supportTicket =
                                            techId?.let { it1 ->
                                                IssueSupportTicket(
                                                    it1,
                                                    option.toString(),
                                                    txtMessage
                                                )
                                            }


                                        val response =
                                            sendCustomerSupportTicketForTechnician(viewModel, supportTicket)

                                        if (response != null) {
                                            if (response.status == 200) {
                                                Toast.makeText(
                                                    context,
                                                    response.message,
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                                navController.navigate(Screen.TechnicianDashboard.route)

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

                                }


                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.arrow_drop_right),
                                    contentDescription = "send message icon ",
                                    tint = Color.Unspecified,
                                    modifier = Modifier
                                        .size(50.dp)
                                        .align(Alignment.Bottom)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                }

                Card(
                    modifier = cardModifier
                        .fillMaxHeight(0.95f)
                        .verticalScroll(state = rememberScrollState()),
                    border = BorderStroke(width = 2.dp, Color.White),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFB6C7E3)),

                    ) {
                    Spacer(modifier = Modifier.height(12.dp))

                    if (showCSMembers) {
                        if (responseString.isNotEmpty()) {
                            val jsonArray = JSONArray(responseString)
                            for (i in 0 until jsonArray.length()) {
                                val jsonObject = jsonArray.getJSONObject(i)
                                val name = jsonObject.getString("name")
                                val contactNumber = jsonObject.getString("contactNumber")

                                val supportTicket = CustomerSupportTicket(name, contactNumber)

                                Spacer(modifier = Modifier.height(8.dp))

                                CustomerSupport(
                                    supportTicket,
                                    Modifier.align(Alignment.CenterHorizontally)
                                )

                            }
                        }
                    }
                }
            }
        }
    }

}




suspend fun sendCustomerSupportTicketForTechnician(
    viewModel: MainViewModel,
    supportTicket: IssueSupportTicket?,
): ResponseObject? {
    var response: ResponseObject? = null

    try {
        if (supportTicket != null) {
            viewModel.sendSupportTicket(supportTicket, "supportTicketTechnician") { responseObject ->
                if (responseObject != null) {
                    response = responseObject
                } else {
                    response = ResponseObject(400, "response is null", null)
                }
            }
        }
    } catch (e: SocketTimeoutException) {
        response = ResponseObject(508, "Request time out.\n Please try again.", e.localizedMessage)
    } catch (e: Exception) {
        response = ResponseObject(404, "Exception error.", e.localizedMessage)
    }
    return response
}

