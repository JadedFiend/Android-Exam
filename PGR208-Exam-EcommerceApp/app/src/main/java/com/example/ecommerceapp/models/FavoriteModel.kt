package com.example.ecommerceapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_product")
data class FavoriteModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productId: Int,
    val productName: String,
    val productPrice: String,
    val productDescription: String,
    val productCategory: String,
    val productImage: String,
    val productRating: String,
    val productReviews: Int
)