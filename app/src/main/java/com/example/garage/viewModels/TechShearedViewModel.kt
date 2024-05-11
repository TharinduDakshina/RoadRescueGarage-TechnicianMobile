package com.example.garage.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.garage.repository.TechnicianCommonDetails

class TechShearedViewModel:ViewModel() {

    var techDetails by mutableStateOf<TechnicianCommonDetails?>(null)
        private set

    fun techCommonDetails(techData: TechnicianCommonDetails){
        techDetails = techData
    }
}