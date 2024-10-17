package com.example.ecommerceapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "add_to_cart")
data class AddToCartModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productId: Int,
    val productName: String,
    val productPrice: String,
    val productCategory: String,
    val productImage: String,
    var productQuantity: Int
)