package com.example.garage.views

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.garage.viewModels.SharedViewModel


@Composable
fun Message(
    navController: NavController, navyStatus:String,sharedViewModel: SharedViewModel
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = defaultBackground
    ) {
        val phoneNumber="+94716788537"
        val context= LocalContext.current

        Header(menuClicked = {})
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = "contact icon",
                tint = Color(0xFF253555),
                modifier = Modifier
                    .padding(0.dp,0.dp,80.dp,0.dp)
                    .clickable {
                        val intent=Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                        context.startActivity(intent)
                    }
            )

            Text(
                text = "Message",
                style = textStyle1,
                fontSize = 26.sp,
                modifier = Modifier.padding(0.dp,0.dp,110.dp,0.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = cardDefaultModifier.align(Alignment.CenterHorizontally),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFB6C7E3)),
            border = BorderStroke(width = 2.dp, Color.White),
        ) {

//            Box(
//                modifier = Modifier
//                    .align(if (true) Alignment.End else Alignment.Start)
//                    .clip(
//                        RoundedCornerShape(
//                            topStart = 48f,
//                            topEnd = 48f,
//                            bottomStart = if (false) 48f else 0f,
//                            bottomEnd = if (false) 0f else 48f
//                        )
//                    )
//                    .background(PurpleGrey80)
//                    .padding(16.dp)
//            ) {
//                Text(text = "message.textaaaaaaaaaaaaaaaaa")
//            }

        }



        Spacer(modifier = Modifier.height(26.dp))
        Footer(navController,navyStatus)
    }
}