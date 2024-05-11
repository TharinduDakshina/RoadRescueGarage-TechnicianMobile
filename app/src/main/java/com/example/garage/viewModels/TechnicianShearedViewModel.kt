package com.example.garage.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

import com.example.garage.repository.TechnicianDashboardServiceCommonDetails

class TechnicianShearedViewModel:ViewModel() {
    var technician by mutableStateOf<TechnicianDashboardServiceCommonDetails?>(null)
        private set

    fun techCommonDetails(techServiceData: TechnicianDashboardServiceCommonDetails){
        technician = techServiceData
    }
}