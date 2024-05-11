package com.example.garage.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.garage.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CircularProcessingBar(isShow:Boolean) {
    if (isShow) {
        var rotationState by remember { mutableStateOf(0f) }

        LaunchedEffect(Unit) {
            while (true) {
                delay(20)
                rotationState += 5f
            }
        }

        AlertDialog(
            onDismissRequest = { },
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.loading_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .rotate(rotationState),
                    colorFilter = ColorFilter.tint(Color(0xFF171E2C))
                )
            }
        }
    }
}