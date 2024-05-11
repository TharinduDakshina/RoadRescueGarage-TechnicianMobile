package com.example.garage.views


import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.garage.models.GarageTechnician
import com.example.garage.models.ResponseObject
import com.example.garage.repository.Screen
import com.example.garage.repository.TechData
import com.example.garage.viewModels.LoginShearedViewModel
import com.example.garage.viewModels.MainViewModel
import com.example.garage.viewModels.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.net.SocketTimeoutException

@Composable
fun TechniciansList(
    navController: NavController, navyStatus: String, sharedViewModel: SharedViewModel,loginShearedViewModel: LoginShearedViewModel
) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val viewModel = viewModel<MainViewModel>()
    val coroutineScope = rememberCoroutineScope()
    var showLoadTechnicians by remember { mutableStateOf(false) }
    var showMessageDialog by remember { mutableStateOf(false) }
//    var showProgressBar by remember { mutableStateOf(true) }

    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var buttonOneName by remember { mutableStateOf("") }
    var buttonTwoName by remember { mutableStateOf("") }
    var techList by remember { mutableStateOf("") }


    LaunchedEffect(Unit) {

        val response = loginShearedViewModel.loginId?.let { loadAllTechnicians(viewModel, it,) }
        Log.d("res double checked", response.toString())
        if (response != null) {
            if (response.status == 200) {

                techList = response.data!!.toString()
                showLoadTechnicians = true

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
                Footer(navController, navyStatus)
            }
        ) {
            Column(
                modifier = defaultBackground.padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {


                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    CommonButton(
                        btnName = "Add Technician",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .width(156.dp)
                            .height(50.dp),
                        onClickButton = {
                            navController.navigate(route = Screen.AddTechnician.route)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

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
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(text = "Name", style = textStyle1, modifier = Modifier)

                        }


                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .weight(0.8f),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(text = "Status", style = textStyle1, modifier = Modifier)

                        }

                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .weight(1.3f),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(text = "", style = textStyle1, modifier = Modifier)

                        }
                    }



                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {

                        // load all technicians in the table
                        if (showLoadTechnicians) {

                            val jsonArray = JSONArray(techList)



                            for (i in 0 until jsonArray.length()) {
                                val jsonObject = jsonArray.getJSONObject(i)
                                val techId = jsonObject.getString("techId")
                                val techFirstName = jsonObject.getString("techFirstName")
                                val techLastName = jsonObject.getString("techLastName")
                                val techStatus = jsonObject.getString("techStatus")
                                val techContactNumber = jsonObject.getString("techContactNumb")
                                val techProfileRef = jsonObject.getString("techProfilePicRef")
                                val techExpertiseList = jsonObject.getString("expertiseList")

                                val contentString =
                                    techExpertiseList.substring(1, techExpertiseList.length - 1)
                                val resultList = contentString.split(", ")


                                val techDetails = GarageTechnician()
                                techDetails.setTechId(techId)
                                techDetails.setTechFirstName(techFirstName)
                                techDetails.setTechLastName(techLastName)
                                techDetails.setTechContactNumber(techContactNumber)
                                techDetails.setTechImageRef(techProfileRef)
                                techDetails.setTechExpertiseAreas(resultList)

                                if (techStatus.equals("Available")) {
                                    techDetails.setTechStatus(1)
                                } else if(techStatus.equals("Assign for Job")){
                                    techDetails.setTechStatus(2)
                                }else{
                                    techDetails.setTechStatus(0)
                                }

                                TechniciansLoadStretcher(
                                    techDetails,
                                    navController,
                                    navyStatus,
                                    coroutineScope,
                                    viewModel,
                                    sharedViewModel
                                )
                                HorizontalDivider()
                            }
                        }
                    }


                    // load response message
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

                Spacer(modifier = Modifier.height(26.dp))

            }
        }
    }


}


@Composable
fun TechniciansLoadStretcher(
    technician: GarageTechnician,
    navController: NavController,
    navyStatus: String,
    coroutineScope: CoroutineScope,
    viewModel: MainViewModel,
    sharedViewModel: SharedViewModel,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var showMessageDialog by remember { mutableStateOf(false) }
        var showDeleteDialog by remember { mutableStateOf(false) }
        var showInfoDialog by remember { mutableStateOf(false) }

        var status by remember { mutableStateOf(0) }
        var title by remember { mutableStateOf("") }
        var message by remember { mutableStateOf("") }
        var buttonOneName by remember { mutableStateOf("") }
        var buttonTwoName by remember { mutableStateOf("") }

        Box(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
                .padding(8.dp, 0.dp),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = technician.getTechFirstName() + " " + technician.getTechLastName(),
                textAlign = TextAlign.Center,
                fontSize = 17.sp,
                color = Color.Black,
                modifier = Modifier,
                maxLines = 2
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
                text = changeStatusType(technician),
                textAlign = TextAlign.Center,
                fontSize = 17.sp,
                color = Color.Black,
                modifier = Modifier,
                maxLines = 2
            )

        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.3f),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {

                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                        modifier = Modifier
                            .background(Color(0x006DBCE9), shape = CircleShape)
                            .weight(1f)
                            .size(25.dp),
                        tint = Color.Black
                    )
                }

                // call to edit technician

                IconButton(onClick = {
                    val technicianData = TechData(
                        techId = technician.getTechId(),
                        techFirstName = technician.getTechFirstName(),
                        techLastName = technician.getTechLastName(),
                        techProfileRef = technician.getTechImageRef()
                    )


                    sharedViewModel.techData(technicianData)
                    navController.navigate(route = Screen.EditTechnician.route)
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = null,
                        modifier = Modifier
                            .background(Color(0x006DBCE9), shape = CircleShape)
                            .weight(1f)
                            .size(25.dp),
                        tint = Color.Black
                    )
                }

                IconButton(onClick = { showInfoDialog = true }) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        modifier = Modifier
                            .background(Color(0x006DBCE9), shape = CircleShape)
                            .weight(1f)
                            .size(25.dp),
                        tint = Color.Black
                    )
                }
            }
        }

        // Delete Dialog

        if (showDeleteDialog) {
            Dialog(
                onDismissRequest = { },
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .fillMaxHeight(0.35f)
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
                                text = "Do you want to delete the user account with the name " +
                                        "${technician.getTechFirstName() + " " + technician.getTechLastName()} ? ",
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
                                onClickButton = { showDeleteDialog = false })

                            CommonButton(btnName = "Yes", modifier = Modifier, onClickButton = {
                                // handle technician delete operation

                                showDeleteDialog = false

                                coroutineScope.launch {
                                    try {
                                        viewModel.delTechnician(
                                            technician.getTechId().substringAfter('-')
                                        ) { responseObject ->
                                            if (responseObject != null) {
                                                if (responseObject.status == 200) {
                                                    title = "Success"
                                                    message = responseObject.message.toString()
                                                    buttonOneName = "null"
                                                    buttonTwoName = "null"
                                                    showMessageDialog = true
                                                } else if (responseObject.status == 400) {
                                                    title = "Failed"
                                                    message = responseObject.message.toString()
                                                    buttonOneName = "null"
                                                    buttonTwoName = "null"
                                                    showMessageDialog = true
                                                } else if (responseObject.status == 500) {
                                                    title = responseObject.message.toString()
                                                    message = responseObject.data.toString()
                                                    buttonOneName = "null"
                                                    buttonTwoName = "null"
                                                }
                                            }
                                        }
                                    } catch (e: SocketTimeoutException) {
                                        showMessageDialog = true
                                        title = "TimeOut"
                                        message = e.message.toString()
                                        buttonOneName = "null"
                                        buttonTwoName = "null"
                                    } catch (e: Exception) {
                                        showMessageDialog = true
                                        title = "Failed"
                                        message = e.message.toString()
                                        buttonOneName = "Ok"
                                        buttonTwoName = "null"
                                    }

                                }
                            })

                        }


                    }
                }
            )
        }

        // more info
        if (showInfoDialog) {
            Dialog(
                onDismissRequest = { showInfoDialog = false },
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .fillMaxHeight(0.35f)
                            .background(
                                Color(0xFFACB3C0),
                                shape = RoundedCornerShape(20.dp)
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {


                            IconButton(onClick = { showInfoDialog = false }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "close icon",
                                    modifier = closerButtonStyles.clickable {
                                        showInfoDialog = false
                                    },
                                    tint = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    ) {
                                        append("•  ")
                                    }
                                    append("Tech Id ")

                                }, color = Color.Black, modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp, 0.dp)
                            )
                            Text(
                                text = technician.getTechId(),
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
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    ) {
                                        append("•  ")
                                    }
                                    append("Name ")

                                }, color = Color.Black, modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp, 0.dp)
                            )
                            Text(
                                text = technician.getTechFirstName() + " " + technician.getTechLastName(),
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
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    ) {
                                        append("•  ")
                                    }
                                    append("Contact ")

                                }, color = Color.Black, modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp, 0.dp)
                            )
                            Text(
                                text = technician.getTechContactNumber(),
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
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    ) {
                                        append("•  ")
                                    }
                                    append("Status ")

                                }, color = Color.Black, modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp, 0.dp)
                            )
                            Text(
                                text = changeStatusType(technician),
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
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    ) {
                                        append("•  ")
                                    }
                                    append("Expertise Areas")

                                }, color = Color.Black, modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp, 0.dp)
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                //Check ExpertiseAreas list isEmpty and list load
                                technician.getTechExpertiseAreas().forEach { temp ->
                                    Text(
                                        text = temp,
                                        color = Color.Black,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier,
                                        maxLines = 2
                                    )
                                }
                            }
                        }

                    }
                }
            )
        }

        // load response message
        if (showMessageDialog) {
            sweetAlertDialog(
                title = title,
                message = message,
                buttonOneName = buttonOneName,
                buttonTwoName = buttonTwoName,
                onConfirm = {
                    showMessageDialog = false
                    navController.navigate(route = Screen.TechnicianList.route)
                }
            )
        }


    }
}


suspend fun loadAllTechnicians(viewModel: MainViewModel,garageId:String): ResponseObject? {
    var response: ResponseObject? = null

    try {
        viewModel.getTechnicians(garageId, "getAll") { responseObject ->
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
