package com.example.garage.repository

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
class GarageCommonDetails (
    val garageId:String,
    val garageName:String,
    val garageContactNumber:String,
    val garageStatus:String,
    val garageEmail:String,
    val garageRating:String,
    val garageType:String,
    val garageOwner:String,
    val garageProfileImageRef:String
):Parcelable