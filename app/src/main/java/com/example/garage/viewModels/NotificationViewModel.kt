package com.example.garage.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.garage.repository.NotificationHelper

class NotificationViewModel : ViewModel() {

    fun initNotificationChannel(context: Context) {
        NotificationHelper.createNotificationChannel(context)
    }

    fun sendNotification(context: Context, title: String, message: String) {
        NotificationHelper.sendNotification(context, title, message)
    }
}