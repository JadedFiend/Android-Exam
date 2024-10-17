package com.example.ecommerceapp.order_history

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.ecommerceapp.models.AddToCartModel
import com.example.ecommerceapp.models.OrderHistoryModel

@Dao
interface OrderHistoryDao {
    @Insert
    suspend fun insertOrderHistory(orderHistoryModel: OrderHistoryModel)

    @Query("SELECT * FROM order_history")
    fun getOrderHistory(): LiveData<List<OrderHistoryModel>>

}