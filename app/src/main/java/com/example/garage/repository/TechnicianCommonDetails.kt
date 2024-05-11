package com.example.garage.repository

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class TechnicianCommonDetails (
    val techFName:String,
    val techLName:String,
    val techContactNumber:String,
    val imgRef:String,
    val techEmail:String,
    val techExpatiateList:List<String>
): Parcelable
