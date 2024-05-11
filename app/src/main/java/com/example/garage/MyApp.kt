package com.example.garage

import android.app.Application
import com.google.firebase.FirebaseApp

class MyApp:Application() {
    override fun onCreate() {
        FirebaseApp.initializeApp(this)
        super.onCreate()
    }
}