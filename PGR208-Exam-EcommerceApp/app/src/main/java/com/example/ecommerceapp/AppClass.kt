package com.example.ecommerceapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class AppClass : Application() {
    companion object {
        lateinit var sharedPreferences: SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }
}