package com.example.garage.views.TechnicianApp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.garage.viewModels.LoginShearedViewModel
import com.example.garage.views.Header
import com.example.garage.views.cardModifier
import com.example.garage.views.defaultBackground
import com.example.garage.views.textStyle4
import kotlinx.coroutines.launch

@Composable
fun SettingsTechnicianScreen(
    navController: NavController,
    loginShearedViewModel: LoginShearedViewModel
)
{

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                modifier = defaultBackground.padding(it),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Column{
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Settings",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        style = textStyle4,
                        fontSize = 32.sp
                    )
            SettingsBox(navController, loginShearedViewModel = loginShearedViewModel)
                }
            }
        }
    }


}

@Composable
fun SettingsBox(
    navController: NavController,
    loginShearedViewModel: LoginShearedViewModel
) {

    var showChangePhoneNumWindow by remember { mutableStateOf(false) }

    Card(
        modifier = cardModifier,
        border = BorderStroke(width = 2.dp, Color.White), shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFB6C7E3)) // Apply shadow to the outer Box
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            FillDetailsButton(detailButtonName = "Change Phone Number") {
                showChangePhoneNumWindow = true
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if(showChangePhoneNumWindow) {
        ChangeTechnicianPhoneNumWindow (navController = navController,loginShearedViewModel){
            showChangePhoneNumWindow = false
        }
    }
}

@Composable
fun FillDetailsButton(detailButtonName: String, onClickButton: () -> Unit) {
    Button(
        onClick = { onClickButton() },
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp), modifier = Modifier
            .width(285.dp)
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
    ) {
        Text(
            text = detailButtonName,
            color = Color(0xFF253555),
            style = textStyle4.copy(textAlign = TextAlign.Center),
            maxLines = 1
        )
    }
}