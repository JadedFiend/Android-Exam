package com.example.ecommerceapp.shared_prefs

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.ecommerceapp.AppClass

class PrefsManager {

    private val sharedPreferences: SharedPreferences =
        AppClass.sharedPreferences

    fun setInsertProductsOnce(check: Boolean) {
        sharedPreferences.edit {
            putBoolean(PrefsKeys.INSERT_ALL_PRODUCTS_ONCE.name, check)
        }
    }

    fun getInsertProductsOnce(): Boolean {
        return sharedPreferences.getBoolean(PrefsKeys.INSERT_ALL_PRODUCTS_ONCE.name, true)
    }


    enum class PrefsKeys {
        INSERT_ALL_PRODUCTS_ONCE
    }
}