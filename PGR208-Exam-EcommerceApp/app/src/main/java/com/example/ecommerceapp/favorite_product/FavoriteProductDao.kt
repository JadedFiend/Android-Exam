package com.example.ecommerceapp.favorite_product

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.ecommerceapp.models.FavoriteModel

@Dao
interface FavoriteProductDao {
    @Insert
    suspend fun insertFavoriteProduct(favoriteModel: FavoriteModel)

    @Query("SELECT * FROM favorite_product")
    fun getFavoriteProduct(): LiveData<List<FavoriteModel>>

    @Delete
    suspend fun deleteFavoriteProduct(favoriteModel: FavoriteModel)
}