package com.example.garage.views

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.garage.models.ActivityModel
import com.example.garage.models.ResponseObject
import com.example.garage.viewModels.LoginShearedViewModel
import com.example.garage.viewModels.MainViewModel
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.net.SocketTimeoutException

@Composable
fun Activities(
    navController: NavController,
    loginShearedViewModel: LoginShearedViewModel,
    navStatus:String
){
    var viewModel:MainViewModel= viewModel()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var buttonOneName by remember { mutableStateOf("") }
    var buttonTwoName by remember { mutableStateOf("") }

    var showMessageDialog by remember { mutableStateOf(false) }
    var showActivities by remember { mutableStateOf(false) }
    var activitiesResponseString by remember { mutableStateOf("") }
    var activity=ActivityModel()

    LaunchedEffect(Unit){
        val response=loadActivities(viewModel)
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
            Column (
                modifier = defaultBackground.padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,

                ){

                Spacer(modifier = Modifier.height(32.dp))

                Text(text = "Activities", style = textStyle4, modifier = Modifier, fontSize = 26.sp)

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = cardDefaultModifier
                        .align(Alignment.CenterHorizontally)
                        .verticalScroll(rememberScrollState()),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFB6C7E3)),
                    border = BorderStroke(width = 2.dp, Color.White),
                ) {
                    Column (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){

                        if (showActivities) {
                            if (activitiesResponseString.isNotEmpty()) {
                                val jsonArray=JSONArray(activitiesResponseString)
                                for (i in 0 until jsonArray.length()) {
                                    val jsonObject = jsonArray.getJSONObject(i)
                                    val date = jsonObject.getString("date")
                                    val time = jsonObject.getString("time")
                                    val customerName = jsonObject.getString("customerName")
                                    val vehicle = jsonObject.getString("vehicle")
                                    val technicianId = jsonObject.getString("technicianId")
                                    val technicianName = jsonObject.getString("technicianName")
                                    val desc = jsonObject.getString("description")
                                    val amount = jsonObject.getDouble("amount")

                                    activity.setDate(date)
                                    activity.setTime(time)
                                    activity.setCustomerName(customerName)
                                    activity.setVehicle(vehicle)
                                    activity.setTechnicianId(technicianId)
                                    activity.setTechnicianName(technicianName)
                                    activity.setDescription(desc)
                                    activity.setAmount(amount)

                                    Spacer(modifier = Modifier.height(16.dp))

                                    ActivityCard(activity,Modifier.align(Alignment.CenterHorizontally),viewModel)
                                }

                            }

                        }
                    }
                }

                Spacer(modifier = Modifier.height(26.dp))

            }
        }
    }



}




@Composable
fun ActivityCard(
    activityModel: ActivityModel,
    modifier: Modifier,
    viewModel: MainViewModel
    ){
    Card(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .shadow(elevation = 8.dp, shape = RectangleShape),
        colors = CardDefaults.cardColors(containerColor = Color.White),

        ) {

        var showDialog by remember { mutableStateOf(false) }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "You have new service request ${activityModel.getTime()}",
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
            Text(text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                ) {
                    append("•  ")
                }
                append("Date")

            }, color = Color.Black, modifier = Modifier
                .weight(1f)
                .padding(8.dp, 0.dp))
            Text(text = activityModel.getDate(),color = Color.Black, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                ) {
                    append("•  ")
                }
                append("Customer Name")

            }, color = Color.Black, modifier = Modifier
                .weight(1f)
                .padding(8.dp, 0.dp))
            Text(text = activityModel.getCustomerName(),color = Color.Black, modifier = Modifier.weight(1f), maxLines = 3)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                ) {
                    append("•  ")
                }
                append("Vehicle")

            }, color = Color.Black, modifier = Modifier
                .weight(1f)
                .padding(8.dp, 0.dp))
            Text(text = activityModel.getVehicle(),color = Color.Black, modifier = Modifier.weight(1f),)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                ) {
                    append("•  ")
                }
                append("Technician Name")

            }, color = Color.Black, modifier = Modifier
                .weight(1f)
                .padding(8.dp, 0.dp))
            Text(text = activityModel.getTechnicianName(),color = Color.Black, modifier = Modifier.weight(1f),)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                ) {
                    append("•  ")
                }
                append("Amount")

            }, color = Color.Black, modifier = Modifier
                .weight(1f)
                .padding(8.dp, 0.dp))
            Text(text = "LKR ${activityModel.getAmount()}0",color = Color.Black, modifier = Modifier.weight(1f),)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                showDialog=true
            }) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription ="Info icon",
                    tint = Color.Black
                )
            }


            if (showDialog){
                Dialog(
                    onDismissRequest = {  },
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



                            Row (
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ){
                                IconButton(onClick = { showDialog = false  }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "close icon",
                                        modifier = closerButtonStyles,
                                        tint = Color.White
                                    )
                                }
                            }


                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    ) {
                                        append("•  ")
                                    }
                                    append("Date")

                                }, color = Color.Black, modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp, 0.dp))
                                Text(text = activityModel.getDate(),color = Color.Black, modifier = Modifier.weight(1f))
                            }

                            Spacer(modifier = Modifier.height(8.dp))


                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    ) {
                                        append("•  ")
                                    }
                                    append("Time")

                                }, color = Color.Black, modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp, 0.dp))
                                Text(text = activityModel.getTime(),color = Color.Black, modifier = Modifier.weight(1f))
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    ) {
                                        append("•  ")
                                    }
                                    append("Customer Name")

                                }, color = Color.Black, modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp, 0.dp))
                                Text(text = activityModel.getCustomerName(),color = Color.Black, modifier = Modifier.weight(1f), maxLines = 3)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    ) {
                                        append("•  ")
                                    }
                                    append("Vehicle")

                                }, color = Color.Black, modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp, 0.dp))
                                Text(text = activityModel.getVehicle(),color = Color.Black, modifier = Modifier.weight(1f),)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    ) {
                                        append("•  ")
                                    }
                                    append("Description ")

                                }, color = Color.Black, modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp, 0.dp))
                                Text(text = if(activityModel.getDescription().isEmpty()) "No Description" else activityModel.getDescription(),color = Color.Black, modifier = Modifier.weight(1f), maxLines = 3)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    ) {
                                        append("•  ")
                                    }
                                    append("Technician Id")

                                }, color = Color.Black, modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp, 0.dp))
                                Text(text = activityModel.getTechnicianId(),color = Color.Black, modifier = Modifier.weight(1f),)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    ) {
                                        append("•  ")
                                    }
                                    append("Technician Name")

                                }, color = Color.Black, modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp, 0.dp))
                                Text(text = activityModel.getTechnicianName(),color = Color.Black, modifier = Modifier.weight(1f),)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    ) {
                                        append("•  ")
                                    }
                                    append("Amount")

                                }, color = Color.Black, modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp, 0.dp))
                                Text(text = "${activityModel.getAmount()}",color = Color.Black, modifier = Modifier.weight(1f),)
                            }

                            Spacer(modifier = Modifier.height(8.dp))


                        }
                    }
                )
            }
        }

    }
}


suspend fun loadActivities(viewModel: MainViewModel):ResponseObject? {
    var response: ResponseObject? = null
    try {
        viewModel.getActivities("","activities") { responseObject ->
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