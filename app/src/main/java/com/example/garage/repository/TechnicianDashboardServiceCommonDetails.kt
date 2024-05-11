package com.example.garage.repository

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
class TechnicianDashboardServiceCommonDetails (
    val techId:String,
    val serviceId:String,
    val time:String,
    val description:String,
    val issueCategory:String,
    val customerName:String,
    val customerContact:String,
    val vehicleModel:String,
    val customerLocation:String
): Parcelable
