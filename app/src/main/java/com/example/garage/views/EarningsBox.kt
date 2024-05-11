package com.example.garage.views

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.garage.R
import com.example.garage.models.BankDetail
import com.example.garage.models.ResponseObject
import com.example.garage.repository.Screen
import com.example.garage.viewModels.LoginShearedViewModel
import com.example.garage.viewModels.MainViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.SocketTimeoutException


@Composable
fun EarningsScreen(
    navController: NavController,
    loginShearedViewModel: LoginShearedViewModel,
) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()



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
                Footer(navController, "navStatus")
            }
        ) {
            Column(
                backgroundModifier
                    .padding(it)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    backgroundModifier,
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(26.dp))
                        Text(
                            text = "Earnings",
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            style = textStyle1,
                            fontSize = 32.sp
                        )
                        EarningsBox(navController,loginShearedViewModel)
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }


}

@Composable
fun EarningsBox(
    navController: NavController,
    loginShearedViewModel: LoginShearedViewModel
) {

    var dayEarningsWindow by remember { mutableStateOf(false) }
    var dayCardEarningsWindow by remember { mutableStateOf(false) }
    var monthlyEarningWindow by remember { mutableStateOf(false) }
    var bankDetailsWindow by remember { mutableStateOf(false) }
    var addedBankDetailsWindow by remember { mutableStateOf(false) }
    val viewModel: MainViewModel = viewModel()
    val garageId = loginShearedViewModel.loginId
    val context= LocalContext.current

    var dalyAmount by remember { mutableStateOf("") }
    var dalyCardAmount by remember { mutableStateOf("") }
    var monthlyAmount by remember { mutableStateOf("") }
    var accountNumber by remember { mutableStateOf("###-######-###") }


    LaunchedEffect(Unit) {
        if (garageId != null) {
            val response = loadEarnings(viewModel, garageId)
            if (response!=null){
                if (response.status == 200) {

                    val jsonObject = JSONObject(response.data.toString())

                    dalyAmount   = jsonObject.getString("dalyEarning")
                    monthlyAmount  = jsonObject.getString("monthlyEarning")
                    dalyCardAmount  = jsonObject.getString("dalyEarningCard")
                    accountNumber = jsonObject.getString("accountNumber")


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
            }else{
                Toast.makeText(
                    context,
                    "Cannot connect the server",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    Card(
        modifier = cardModifier,
        border = BorderStroke(width = 2.dp, Color.White),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD3EFFF))// Apply shadow to the outer Box
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            AccountFieldButton(
                labelName = "Today's earnings",
                value = dalyAmount,
                onClickButton = { dayEarningsWindow = true }
            )
            AccountFieldButton(
                labelName = "Today's earnings (card)",
                value = dalyCardAmount,
                onClickButton = { dayCardEarningsWindow = true }
            )

            AccountFieldButton(
                labelName = "30-day earnings",
                value = monthlyAmount,
                onClickButton = { monthlyEarningWindow = true }
            )

            AccountFieldButton(
                labelName = "Bank details",
                value = accountNumber,
                onClickButton = { addedBankDetailsWindow = true }
            )

            Spacer(modifier = Modifier.height(12.dp))

            CommonButton(
                btnName = "Add Bank Details", modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(190.dp)
            ) {
                bankDetailsWindow = true
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
    if (dayEarningsWindow) {
        MoreInfoWindow(message = "Total earnings for today") {
            dayEarningsWindow = false
        }
    }
    if (dayCardEarningsWindow) {
        MoreInfoWindow(message = "Total earnings by card (credit/debit) today. You will be paid automatically at 11:55 PM.") {
            dayCardEarningsWindow = false
        }
    }
    if (monthlyEarningWindow) {
        MoreInfoWindow(message = "Total earnings for the past 30 days") {
            monthlyEarningWindow = false
        }
    }

    if (bankDetailsWindow) {
        if (garageId != null) {
            BankDetailsWindow(navController,viewModel,garageId) {
                bankDetailsWindow = false
            }
        }
    }

    if (addedBankDetailsWindow) {
        MoreInfoWindow(message = "This field will remain empty if you have not added your bank details.") {
            addedBankDetailsWindow = false
        }
    }
}

suspend fun loadEarnings(viewModel: MainViewModel, garageId: String): ResponseObject? {
    var response: ResponseObject? = null

    try {
        viewModel.getGarageDetails(garageId, "getEarningDetails") { responseObject ->
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountFieldButton(labelName: String, value: String, onClickButton: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = labelName,
                style = textStyle1,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Card(
                onClick = { onClickButton() },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                border = BorderStroke(width = 2.dp, color = Color.White),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .padding(horizontal = 38.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFC6D4DE))
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
                ) {
                    Text(
                        text = value,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .weight(1f),
                        maxLines = 1,
                        style = textStyle2,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.question_fill),
                        modifier = Modifier
                            .size(30.dp),
                        contentDescription = "Info",
                        tint = Color.Unspecified
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun BankDetailsWindow(
    navController: NavController,
    viewModel: MainViewModel,
    garageId: String,
    onDismiss: () -> Unit
) {
    var accountNumber by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var bank by remember { mutableStateOf("") }
    var branch by remember { mutableStateOf("") }

    val containScope= rememberCoroutineScope()

    var context = LocalContext.current

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
                    text = "Add your bank details",
                    style = textStyle2
                )
                Spacer(modifier = Modifier.height(16.dp))

                accountNumber =
                    AccountDetailsField(labelName = "Account number (required)", value = "")
                name = AccountDetailsField(labelName = "Name (required)", value = "")

                val bankList =
                    listOf("BOC", "Commercial Bank", "DFCC", "HNB", "NDB", "NSB", "Sampath Bank")

                bank = CommonDropdown(bankList, "Bank",
                    Modifier
                        .width(230.dp)
                        .height(50.dp)).toString()

                branch = AccountDetailsField(labelName = "Branch (required)", value = "")

                Spacer(modifier = Modifier.height(8.dp))


                Spacer(modifier = Modifier.height(16.dp))
                CommonButton(
                    btnName = "Add",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    if (bank.isNotEmpty() && name != "" && branch != "" && accountNumber != "") {
                        if (accountNumber.toLong()> 10){

                            val bankDetail=BankDetail(
                                bank,name,branch,accountNumber
                            )

                            containScope.launch {
                               val response = addBankDetails(viewModel,bankDetail,garageId)
                                if (response!=null){
                                    if (response.status == 200) {

                                        Toast.makeText(
                                            context,
                                            "Successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        navController.navigate(Screen.Earning.route)


                                    } else if (response.status == 204) {
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

                                    }else if (response.status == 404) {
                                        Toast.makeText(
                                            context,
                                            response.message,
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    } else if (response.status == 500) {
//                                        Toast.makeText(
//                                            context,
//                                            response.message,
//                                            Toast.LENGTH_SHORT
//                                        ).show()
                                    } else if (response.status == 508) {
                                        Toast.makeText(
                                            context,
                                            response.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }else{
                                    Toast.makeText(
                                        context,
                                        "Cannot connect the server",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }
                        }else{
                            Toast.makeText(
                                context,
                                "Account number is invalided",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        //todo
                        onDismiss()
                    } else {
                        MainScope().launch {
                            Toast.makeText(
                                context,
                                "Fill all the required details",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    )
}

suspend fun addBankDetails(viewModel: MainViewModel, bankDetail: BankDetail,garageId:String):ResponseObject? {
    var response: ResponseObject? = null

    try {
        viewModel.addBankDetails(garageId,bankDetail) { responseObject ->
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
fun AccountDetailsField(labelName: String, value: String?): String {
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
                modifier = Modifier
                    .padding(horizontal = 0.dp)
                    .height(50.dp)
                    .border(2.dp, Color.White, shape = RoundedCornerShape(50))
                    .shadow(6.dp, shape = RoundedCornerShape(50))
                    .background(Color.White),
                textStyle = textStyle2,
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