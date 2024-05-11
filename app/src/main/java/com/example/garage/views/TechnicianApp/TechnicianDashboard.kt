package com.example.garage.views.TechnicianApp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.garage.models.ResponseObject
import com.example.garage.models.TechnicianDashboard
import com.example.garage.repository.Screen
import com.example.garage.repository.TechnicianCommonDetails
import com.example.garage.repository.TechnicianDashboardServiceCommonDetails
import com.example.garage.viewModels.LoginShearedViewModel
import com.example.garage.viewModels.MainViewModel
import com.example.garage.viewModels.TechShearedViewModel
import com.example.garage.viewModels.TechnicianShearedViewModel
import com.example.garage.views.CircularProcessingBar
import com.example.garage.views.CommonButton
import com.example.garage.views.Header
import com.example.garage.views.defaultBackground
import com.example.garage.views.textStyle4
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.SocketTimeoutException

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TechnicianDashboard(
    navController: NavController,
    navStatus: String,
    loginShearedViewModel: LoginShearedViewModel,
    technicianShearedViewModel: TechnicianShearedViewModel,
    techShearedViewModel:TechShearedViewModel
){
    val techId=loginShearedViewModel.loginId
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showTechnicianJobs by remember { mutableStateOf(false) }
    var processingBarStatus = remember { mutableStateOf(true) }
    val showTechProfileData = remember { mutableStateOf(false) }
    val viewModel:MainViewModel= viewModel()
    var responseString by remember { mutableStateOf("") }
    var technicianData by remember { mutableStateOf("") }
    val context= LocalContext.current

    LaunchedEffect(Unit){

        val response =loginShearedViewModel.loginId?.let { loadTechnicianData(viewModel, it) }

        if (response != null) {
            if (response.status == 200) {

                technicianData = response.data!!.toString()
                showTechProfileData.value=true

                Log.d("TAG", "TechnicianProfile: $technicianData")


            } else if (response.status == 400) {
                Toast.makeText(
                    context,
                    response.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()

            } else if (response.status == 404) {
                Toast.makeText(
                    context,
                    response.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()

            } else if (response.status == 500) {
                Toast.makeText(
                    context,
                    response.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (response.status == 508) {
                Toast.makeText(
                    context,
                    response.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    response.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                context,
                "Can not call the server.",
                Toast.LENGTH_SHORT
            ).show()
        }




        if (techId != null) {
            val response =loadServices(viewModel,techId)
            if (response != null) {
                if (response.status == 200) {
                    responseString = response.data!!.toString()
                    processingBarStatus.value=false
                    showTechnicianJobs = true

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
        }
    }

    if (showTechProfileData.value) {
        try {
            val jsonObject = JSONObject(technicianData)

            val techFName = jsonObject.getString("techFName")
            val techLName = jsonObject.getString("techLName")
            val tecEmail = jsonObject.getString("tecEmail")
            val phoneNumber = jsonObject.getString("phoneNumber")
            val imgRef = jsonObject.getString("imgRef")
            val techExpertiseList = jsonObject.getString("expertiseList")

            val contentString =
                techExpertiseList.substring(1, techExpertiseList.length - 1)
            val resultList = contentString.split(", ")


           val temp= TechnicianCommonDetails(
                techFName,
                techLName,
                phoneNumber,
                imgRef,
                tecEmail,
                resultList
            )

            techShearedViewModel.techCommonDetails(temp)


        } catch (e: JSONException) {
            e.localizedMessage?.let { it1 -> Log.d("json error", it1) }
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
                TechnicianFooter(navController, navStatus)
            }
        ) {

            CircularProcessingBar(isShow = processingBarStatus.value)

            Column(
                modifier = defaultBackground.padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = techShearedViewModel.techDetails?.techFName +" "+techShearedViewModel.techDetails?.techLName,
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

                    if (showTechnicianJobs) {
                        if (responseString.isNotEmpty()) {
                            val jsonArray = JSONArray(responseString)
                            for (i in 0 until jsonArray.length()) {
                                val jsonObject = jsonArray.getJSONObject(i)
                                val time = jsonObject.getString("time")
                                val description = jsonObject.getString("description")
                                val issueCategory = jsonObject.getString("issueCategory")
                                val customerName = jsonObject.getString("customerName")
                                val customerContact = jsonObject.getString("customerContact")
                                val vehicleModel = jsonObject.getString("vehicleModel")
                                val serviceId = jsonObject.getString("serviceId")
                                val customerLocation = jsonObject.getString("customerLocation")
                                val technicianService=TechnicianDashboard(serviceId,time,description,issueCategory,customerName,customerContact,vehicleModel,customerLocation)

                                Spacer(modifier = Modifier.height(16.dp))

                                if (techId != null) {
                                    ServiceRequest(
                                        techId,
                                        technicianService,
                                        navController,
                                        Modifier.align(Alignment.CenterHorizontally),
                                        context,
                                        technicianShearedViewModel,
                                        true,
                                        customerLocation
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




@Composable
fun ServiceRequest(
    techId:String,
    technicianService:TechnicianDashboard,
    navController: NavController,
    modifier: Modifier,
    context: Context,
    technicianShearedViewModel: TechnicianShearedViewModel,
    button:Boolean,
    customerLocation:String
) {

    val phoneNumber = technicianService.getCustomerContact()


    Card(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.35f)
            .shadow(elevation = 8.dp, shape = RectangleShape),
        colors = CardDefaults.cardColors(containerColor = Color.White),

        ) {

        Spacer(modifier = Modifier.height(8.dp))

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = " ${technicianService.getIssueCategory()}",
                color = Color(0xFF253555),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )

            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = "Contact icon",
                tint = Color.Black,
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                    context.startActivity(intent)
                }
            )

        }

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalDivider(thickness = 2.dp, color = Color(0xFF253555))

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Time", color = Color.Black, modifier = Modifier
                    .weight(1f)
                    .padding(8.dp, 0.dp)
            )
            Text(
                text = technicianService.getTime(),
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
                text = "Customer Name", color = Color.Black, modifier = Modifier
                    .weight(1f)
                    .padding(8.dp, 0.dp)
            )
            Text(
                text = technicianService.getCustomerName(),
                color = Color.Black,
                modifier = Modifier.weight(1f),
                maxLines = 2
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
                text = "Vehicle", color = Color.Black, modifier = Modifier
                    .weight(1f)
                    .padding(8.dp, 0.dp)
            )
            Text(
                text = technicianService.getVehicleModel(),
                color = Color.Black,
                modifier = Modifier.weight(1f),
                maxLines = 1
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
                text = technicianService.getDescription().ifEmpty { "No Description" },
                color = Color.Black,
                modifier = Modifier.weight(1f),
                maxLines = 3
            )
        }

        Spacer(modifier = Modifier.height(8.dp))


        if (button) {

            val details= TechnicianDashboardServiceCommonDetails(
                techId,
                technicianService.getServiceId(),
                technicianService.getTime(),
                technicianService.getDescription(),
                technicianService.getIssueCategory(),
                technicianService.getCustomerName(),
                technicianService.getCustomerContact(),
                technicianService.getVehicleModel(),
                customerLocation
            )
            technicianShearedViewModel.techCommonDetails(details)



            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                CommonButton(
                    btnName = "Show Details",
                    modifier = Modifier.align(Alignment.CenterVertically).width(170.dp),
                    onClickButton = {
                        navController.navigate(route = Screen.TechnicianCompleteJob.route)
                    }
                )

            }
        }


        Spacer(modifier = Modifier.height(8.dp))
    }
}

suspend fun loadServices(viewModel: MainViewModel,techId:String):ResponseObject? {
    var response: ResponseObject? = null

    try {
        viewModel.getTechnicianServices(techId, "getTechServices") { responseObject ->
            if (responseObject != null) {
                response = responseObject
            } else {
                response = ResponseObject(400, "response is null", null)
            }
        }
    }catch (e: SocketTimeoutException) {
        response = ResponseObject(508, "Request time out.\n Please try again.", e.localizedMessage)
    } catch (e: Exception) {
        response = ResponseObject(404, "Exception error.", e.localizedMessage)
    }
    return response
}