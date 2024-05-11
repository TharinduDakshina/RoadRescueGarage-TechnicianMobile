package com.example.garage.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.garage.R
import com.example.garage.models.GarageTechnician


val fontFamily= FontFamily(
    Font(R.font.poppins_extra_light,FontWeight.Light),
    Font(R.font.poppins_light_italic,FontWeight.Light),
    Font(R.font.poppins_medium,FontWeight.Medium),
    Font(R.font.poppins_semi_bold,FontWeight.SemiBold),
    Font(R.font.poppins_semi_bold_italic,FontWeight.SemiBold),
)

val textStyle1 = TextStyle(
    fontFamily= fontFamily,
    fontWeight = FontWeight.ExtraBold,
    fontSize = 18.sp,
    letterSpacing = 0.15.sp,
    color = Color(0xFF253555)
)

val textStyle2 = TextStyle(
    fontFamily= fontFamily,
    fontWeight = FontWeight.ExtraBold,
    fontSize = 16.sp,
    letterSpacing = 0.15.sp,
    color = Color(0xFF253555),
)

val textStyle3 = TextStyle(
    fontFamily= fontFamily,
    fontWeight = FontWeight.ExtraBold,
    fontSize = 16.sp,
    letterSpacing = 0.15.sp,
    color = Color.White,
)

val textStyle4=TextStyle(
    fontFamily= fontFamily,
    fontWeight = FontWeight.ExtraBold,
    letterSpacing = 0.15.sp,
    fontSize = 16.sp,
    color = Color(0xFF253555)
)



val textStyle5=TextStyle(
    fontFamily= FontFamily.Serif,
    fontWeight = FontWeight.Medium,
    letterSpacing = 0.15.sp,
    fontSize = 32.sp,
    color = Color.Red
)

val textStyle6 = TextStyle(
    fontFamily= fontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    letterSpacing = 0.15.sp,
    color = Color(0xFFB8BABE),
)


val textStyleDefault=TextStyle(
    fontFamily= FontFamily.Serif,
    fontWeight = FontWeight.Normal,
    letterSpacing = 0.15.sp,
    fontSize = 20.sp,
    color = Color.Black,
    textAlign = TextAlign.Center
)

val backgroundModifier = Modifier
    .fillMaxSize()
    .background(
        brush = Brush.linearGradient(
            colors = listOf(Color(0xFFD3EFFF), Color.White),
            start = Offset(0f, 0f),
            end = Offset(0f, Float.POSITIVE_INFINITY)
        )
    )

val cardModifier = Modifier
    .fillMaxWidth()
    .padding(horizontal = 16.dp, vertical = 8.dp)

val defaultBackground =Modifier
    .fillMaxSize()
    .background(Color(0xFFD3EFFF))

val cardDefaultModifier=Modifier
    .fillMaxWidth(0.84f)
    .fillMaxHeight(0.95f)

 val closerButtonStyles=Modifier
     .background(Color.Transparent, shape = RoundedCornerShape(20.dp))
     .border(BorderStroke(2.dp, Color(0xFF253555)), shape = RoundedCornerShape(20.dp))

val deleteIconStyles=Modifier
    .background(Color.Transparent, shape = RoundedCornerShape(20.dp))
    .border(BorderStroke(2.dp, Color(0xB5D32222)), shape = RoundedCornerShape(20.dp))

fun changeStatusType(technician: GarageTechnician): String {
    return if (technician.getTechStatus()==1){
        "Available"
    }else if (technician.getTechStatus()==2){
        "Assign for job"
    }else{
        "Not Available"

    }
}

