package com.example.garage.views.TechnicianApp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.garage.models.ActivityModel
import com.example.garage.models.ResponseObject
import com.example.garage.viewModels.LoginShearedViewModel
import com.example.garage.viewModels.MainViewModel
import com.example.garage.views.Header
import com.example.garage.views.cardDefaultModifier
import com.example.garage.views.defaultBackground
import com.example.garage.views.textStyle1
import com.example.garage.views.textStyle4
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.SocketTimeoutException


@Composable
fun TechnicianActivities(
    navController: NavController,
    loginShearedViewModel: LoginShearedViewModel
){
    var viewModel: MainViewModel = viewModel()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var buttonOneName by remember { mutableStateOf("") }
    var buttonTwoName by remember { mutableStateOf("") }

    var showMessageDialog by remember { mutableStateOf(false) }
    var showActivities by remember { mutableStateOf(false) }
    var activitiesResponseString by remember { mutableStateOf("") }
    var activity= ActivityModel()

    LaunchedEffect(Unit){
        val response= loginShearedViewModel.loginId?.let { loadTechActivities(viewModel, it) }
        if (response != null) {
            if (response.status == 200) {
                activitiesResponseString = response.data!!.toString()
                showActivities=true

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
                TechnicianFooter(navController, "navStatus")
            }
        ) {
            Column (
                modifier = defaultBackground.padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,

                ){

                Spacer(modifier = Modifier.height(32.dp))

                Text(text = "Jobs Done", style = textStyle4, modifier = Modifier, fontSize = 26.sp)

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = cardDefaultModifier
                        .align(Alignment.CenterHorizontally),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFB6C7E3)),
                    border = BorderStroke(width = 2.dp, Color.White),
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.09f)
                            .background(Color(0xFFD9D9D9)),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .weight(1f).padding(0.dp,0.dp,140.dp,0.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(text = "Expertise", style = textStyle1, modifier = Modifier)

                        }

                    }


                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {

                        if (showActivities){
                            if (activitiesResponseString.isNotEmpty()) {
                                val jsonObject = JSONObject(activitiesResponseString)
                                val ElectricalTroubleShoot = jsonObject.getString("ElectricalTroubleShoot")
                                val engineMaintainable = jsonObject.getString("engineMaintainable")
                                val OilSystem = jsonObject.getString("OilSystem")
                                val HVAC = jsonObject.getString("HVAC")
                                val Other = jsonObject.getString("Other")


                                Spacer(modifier = Modifier.height(20.dp))
                                TechnicianActivityExpatiateLoader(viewModel,"Electrical Troubleshoot",ElectricalTroubleShoot)

                                Spacer(modifier = Modifier.height(20.dp))
                                TechnicianActivityExpatiateLoader(viewModel,"Engine Maintainable",engineMaintainable)

                                Spacer(modifier = Modifier.height(20.dp))
                                TechnicianActivityExpatiateLoader(viewModel,"Oil System",OilSystem)

                                Spacer(modifier = Modifier.height(20.dp))
                                TechnicianActivityExpatiateLoader(viewModel,"HVAC",HVAC)

                                Spacer(modifier = Modifier.height(20.dp))
                                TechnicianActivityExpatiateLoader(viewModel,"Other",Other)

                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(26.dp))

            }
        }
    }
}

suspend fun loadTechActivities(viewModel: MainViewModel, techId: String): ResponseObject? {
    var response: ResponseObject? = null

    try {
        viewModel.loadTechActivities(techId, "fetchActivities") { responseObject ->
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
fun TechnicianActivityExpatiateLoader(
    viewModel:MainViewModel,
    expatice:String,
    count:String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
                .padding(8.dp, 0.dp),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = expatice,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                color = Color(0xFF253555),
                modifier = Modifier,
                maxLines = 2,
                fontWeight = FontWeight.Bold,

            )

        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
                .weight(0.8f),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = count +"Done Jobs",
                textAlign = TextAlign.Center,
                fontSize = 17.sp,
                color = Color(0xFF253555),
                modifier = Modifier,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
            )

        }

    }
}