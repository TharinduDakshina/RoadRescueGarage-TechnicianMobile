package com.example.garage.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.garage.R



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(menuClicked:()->Unit) {
    Column {
        CenterAlignedTopAppBar(
            title = {
                Icon(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Toolbar icon",
                    modifier = Modifier.size(100.dp),
                    tint = Color.Unspecified
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                         menuClicked()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        tint = Color.White,
                        modifier = Modifier.size(30.dp),
                        contentDescription = "Localized description"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(0xFF253555)
            )
        )
    }

}