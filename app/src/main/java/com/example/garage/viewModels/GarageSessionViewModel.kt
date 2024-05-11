package com.example.garage.viewModels

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel



class GarageSessionViewModel(context:Context):ViewModel() {
    private val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    val isLoggedIn = mutableStateOf(sharedPreferences.getBoolean("isLoggedIn", false))


    fun setLoggedIn(isLoggedIn: Boolean) {
        this.isLoggedIn.value = isLoggedIn
        sharedPreferences.edit().putBoolean("isLoggedIn", isLoggedIn).apply()
    }


    /*fun logIn(){
        sessionState.value=true
    }

    fun logOut(){
        sessionState.value=false
    }

    fun getSessionState():Boolean{
        return this.sessionState.value
    }*/
}