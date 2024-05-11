package com.example.garage.views


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CommonButton(btnName:String,modifier: Modifier,onClickButton:()->Unit) {
    Button(
        onClick = { onClickButton() },
        modifier = modifier.width(98.dp),
        border = BorderStroke(width = 2.dp,color= Color.White),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF253555))
    ) {
        Text(
            text = btnName,
            style = textStyle3
            )
    }
}