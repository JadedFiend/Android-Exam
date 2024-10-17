package com.example.ecommerceapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ecommerceapp.add_to_cart.AddToCartDao
import com.example.ecommerceapp.favorite_product.FavoriteProductDao
import com.example.ecommerceapp.models.AddToCartModel
import com.example.ecommerceapp.models.FavoriteModel
import com.example.ecommerceapp.models.OrderHistoryModel
import com.example.ecommerceapp.models.ProductDetails
import com.example.ecommerceapp.order_history.OrderHistoryDao
import com.example.ecommerceapp.product_details.ProductDetailsDao

@Database(
    entities = [ProductDetails::class, AddToCartModel::class, FavoriteModel::class, OrderHistoryModel::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDetailsDao(): ProductDetailsDao

    abstract fun addToCartDao(): AddToCartDao

    abstract fun favoriteProductDao(): FavoriteProductDao

    abstract fun orderHistoryDao(): OrderHistoryDao

}