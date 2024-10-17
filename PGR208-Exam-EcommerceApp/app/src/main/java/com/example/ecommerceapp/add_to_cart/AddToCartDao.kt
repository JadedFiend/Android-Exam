package com.example.ecommerceapp.add_to_cart

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.ecommerceapp.models.AddToCartModel

@Dao
interface AddToCartDao {
    @Insert
    suspend fun insertAddToCart(addToCartModel: AddToCartModel)

    @Query("SELECT * FROM add_to_cart")
    fun getAddToCart(): LiveData<List<AddToCartModel>>

    @Delete
    suspend fun deleteFromCart(addToCartModel: AddToCartModel)

    @Update
    suspend fun updateProductQuantity(addToCartModel: AddToCartModel)
}