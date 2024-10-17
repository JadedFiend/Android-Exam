package com.example.ecommerceapp.data


import androidx.room.TypeConverter
import com.example.ecommerceapp.models.OrderedProductModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Converters {
    @TypeConverter
    fun fromJson(json: String?): List<OrderedProductModel> {
        if (json == null) {
            return emptyList()
        }

        val type = object : TypeToken<List<OrderedProductModel>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toJson(list: List<OrderedProductModel>): String {
        return Gson().toJson(list)
    }
}