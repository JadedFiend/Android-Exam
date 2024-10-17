package com.example.ecommerceapp.product_details

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.ecommerceapp.models.ProductDetails

@Dao
interface ProductDetailsDao {
    @Insert
    suspend fun insertAllProducts(productDetails: ProductDetails)

    @Query("SELECT * FROM product_details")
    fun getAllProducts(): LiveData<List<ProductDetails>>
}