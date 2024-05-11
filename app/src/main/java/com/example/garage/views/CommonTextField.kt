package com.example.garage.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTextField(
    value:String,
    isEditing:Boolean,
    placeholderName:String,
    modifier: Modifier,
    prefixStatus:Boolean,
    keyboardType: KeyboardType
):String{
    var textFieldValue by remember { mutableStateOf(value) }
    var clickMoreInformation by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.15f),
        horizontalArrangement = Arrangement.Center,
        //verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.9f),
            contentAlignment = Alignment.Center
        ) {
            OutlinedTextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                textStyle = TextStyle(
                    Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    letterSpacing = 0.15.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = fontFamily
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(10.dp))
                    .border(
                        BorderStroke(0.dp, Color.Unspecified),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .height(IntrinsicSize.Max)
                    .width(IntrinsicSize.Max),
                enabled = isEditing,
                singleLine = true,
                placeholder = {
                    Text(
                        text = placeholderName,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF253555).copy(alpha = 0.5f),
                        modifier = Modifier.padding(15.dp, 0.dp, 0.dp, 0.dp),
                        fontFamily = fontFamily
                    )
                },
                suffix = {
                    if (prefixStatus) {
                        IconButton(onClick = {
                            clickMoreInformation=true

                        }) {
                            Icon(
                                imageVector = Icons.Rounded.Info,
                                contentDescription = "Change ContactNumber",
                                tint = Color(0xFF253555)
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType
                )
            )
        }
        if (clickMoreInformation){
            MoreInfoWindow(
                message = "Change phone number in settings?"
            ){
                clickMoreInformation = false
            }
        }
    }

    return textFieldValue
}

