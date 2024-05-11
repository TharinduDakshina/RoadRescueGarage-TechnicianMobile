package com.example.garage.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.garage.R
import com.example.garage.repository.Screen

@Composable
fun Footer(
    navController: NavController,
    navStatus:String,
){


    Row (
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp))
            .background(color = Color(0xFF253555))
    ){
        Icon(
            painter = painterResource(id = R.drawable.technicins_footer),
            modifier = Modifier
                .padding(16.dp)
                .size(45.dp)
                .clickable {
                    // loadAllTechnicians()
                    navController.navigate(route = Screen.TechnicianList.route)
                },
            tint = Color.White,
            contentDescription = "footer technician"
        )


        Icon(
            painter = painterResource(id = R.drawable.home),
            modifier = Modifier
                .padding(16.dp)
                .size(45.dp)
                .clickable {
                    navController.navigate(route = Screen.GarageDashboard.route)
                }
            ,
            tint = Color.Unspecified,
            contentDescription = "home"
        )

        Icon(
            painter = painterResource(id = R.drawable.chat_fill),
            modifier = Modifier
                .padding(16.dp)
                .size(45.dp)
                .clickable {
                    navController.navigate(route = Screen.Activities.route)
                }
            ,
            tint = Color.Unspecified,
            contentDescription = "garage activities"
        )

        Icon(
            painter = painterResource(id = R.drawable.user_fill),
            modifier = Modifier
                .padding(16.dp)
                .size(45.dp)
                .clickable {
                    navController.navigate(route = Screen.GarageProfile.route)
                }
            ,
            tint = Color.Unspecified,
            contentDescription = "garage profile"
        )

    }
}

