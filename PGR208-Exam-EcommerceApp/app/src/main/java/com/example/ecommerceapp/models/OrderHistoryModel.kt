package com.example.ecommerceapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_history")
data class OrderHistoryModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderTime: String,
    val orderAmount: String,
    val orderedProductQuantity: Int,
    val orderedProductList: List<OrderedProductModel>
)