package com.example.ecommerceapp.order_history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.models.OrderHistoryModel
import com.example.ecommerceapp.data.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderHistoryViewModel(private val repository: DatabaseRepository) : ViewModel() {
    private val _insertSuccess = MutableLiveData<Boolean>()

    fun insertOrderHistory(orderHistoryModel: OrderHistoryModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.insertOrderHistory(orderHistoryModel)
                withContext(Dispatchers.Main) {
                    _insertSuccess.value = true
                }
            }
        }
    }

    fun getOrderHistory(): LiveData<List<OrderHistoryModel>> {
        return repository.getOrderHistory()
    }
}