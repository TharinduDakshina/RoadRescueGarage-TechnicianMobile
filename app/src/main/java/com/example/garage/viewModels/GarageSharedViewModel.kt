package com.example.garage.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.garage.repository.GarageCommonDetails

class GarageSharedViewModel: ViewModel() {
    var garage by mutableStateOf<GarageCommonDetails?>(null)
        private set

    fun garageCommonDetails(garageCommonDetails: GarageCommonDetails){
        garage = garageCommonDetails
    }
}