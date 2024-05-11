package com.example.garage.repository

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class TechData (
    val techId:String,
    val techFirstName:String,
    val techLastName:String,
    val techProfileRef:String
): Parcelable