package com.example.garage.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginShearedViewModel:ViewModel() {
    var loginId by mutableStateOf<String?>(null)
        private set

    fun specificLoginId(id:String){
        loginId = id
    }
}