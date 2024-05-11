package com.example.garage.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.garage.R


@Composable
fun circularIndicatorProgressBar(
) {


    AlertDialog(
        onDismissRequest = {},
        modifier = Modifier
            .fillMaxHeight(0.25f)
            .fillMaxWidth(0.7f),
        text = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.loder_image),
                    contentDescription = "loader image"
                )
            }

        },
        containerColor = Color(0x43F0E9E9),
        confirmButton = { /*TODO*/ }
    )


}