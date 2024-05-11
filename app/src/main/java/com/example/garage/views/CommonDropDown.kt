package com.example.garage.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CommonDropdown(
    optionList:List<Any>,defaultSelection:Any,modifier: Modifier
): Any {

    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(defaultSelection) }


    Box(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.2f)
            .background(Color.White, shape = RoundedCornerShape(20.dp))
            .clickable { expanded = true },
        contentAlignment = Alignment.Center
    ){

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            horizontalArrangement = Arrangement.SpaceAround,

            ) {


            Text("$selectedOption", color = Color(0xFF253555) , style = textStyle4)

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color(0xFF253555),
                )

        }


        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .fillMaxHeight(0.2f)
                .clip(RoundedCornerShape(5.dp))
                .background(Color(0xFFF0F0ED))
                .border(BorderStroke(1.dp, Color.Unspecified))
                .align(Alignment.CenterStart)

        ) {

            optionList.forEach{option ->

                DropdownMenuItem(
                    onClick = {
                        selectedOption = option
                        expanded = false
                    },
                    text = { Text(text = "$option", style = textStyle4, color = Color.Black, modifier = Modifier.align(Alignment.CenterHorizontally)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF0F0ED))
                        .align(Alignment.CenterHorizontally)
                )
                HorizontalDivider()
            }
        }

    }
    return selectedOption
}
