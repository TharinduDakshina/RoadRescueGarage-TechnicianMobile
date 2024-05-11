package com.example.garage.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun sweetAlertDialog(
    title: String,
    message: String?,
    buttonOneName: String?,
    buttonTwoName:String?,
    onConfirm: () -> Unit
) {
    if (buttonOneName.equals("null") && buttonTwoName.equals("null")){
        AlertDialog(
            onDismissRequest = onConfirm,
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    if(title.equals("Success")){
                        Text(
                            text = title,
                            style = textStyle5,
                            color = Color.Green
                        )
                    }else if(title.equals("Failed")){
                        Text(
                            text = title,
                            style = textStyle5,
                            color = Color.Red
                        )
                    }else{
                        Text(
                            text = title,
                            style = textStyle5,
                            color = Color.Black
                        )
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    if (message != null) {
                        Text(
                            text = message,
                            style = textStyle2,
                            color = Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            },
            confirmButton = {},
            containerColor = Color(0xC1CCC0C0)
        )
    }else{
        AlertDialog(
            onDismissRequest = { /* Nothing to do here */ },
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = title,
                        style = textStyle5,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    if (message != null) {
                        Text(
                            text = message,
                            style = textStyle2,
                            color = Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            },
            confirmButton = {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {

                    if (buttonOneName!="null" || buttonTwoName.equals("null")){

                        Button(
                            modifier = Modifier.width(90.dp),
                            onClick = onConfirm,
                            colors = ButtonDefaults.buttonColors(Color.Gray)
                        ) {

                            if (buttonOneName != null) {
                                Text(text = buttonOneName)
                            }

                        }
                    }else{
                        Button(
                            modifier = Modifier.weight(1f).align(Alignment.CenterVertically),
                            onClick = onConfirm,
                            colors = ButtonDefaults.buttonColors(Color.Green)
                        ) {
                            Text(text = buttonOneName)
                        }

                        Spacer(modifier = Modifier.width(5.dp))

                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.CenterVertically),
                            onClick = onConfirm,
                            colors = ButtonDefaults.buttonColors(Color.Green)
                        ) {
                            if (buttonTwoName != null) {
                                Text(text = buttonTwoName)
                            }
                        }
                    }


                }
            },
            containerColor = Color(0xC1CCC0C0)
        )
    }
}