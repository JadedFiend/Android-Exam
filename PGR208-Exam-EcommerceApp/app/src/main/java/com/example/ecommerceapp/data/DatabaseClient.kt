package com.example.ecommerceapp.data

import android.content.Context
import androidx.room.Room

object DatabaseClient {

    private var appDatabase: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
        return appDatabase ?: synchronized(this) {
            appDatabase ?: buildDatabase(context).also { appDatabase = it }
        }
    }
    private fun buildDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration().build()
    }
}