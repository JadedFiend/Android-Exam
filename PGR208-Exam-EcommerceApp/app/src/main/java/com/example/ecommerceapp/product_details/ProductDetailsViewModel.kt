package com.example.ecommerceapp.product_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.models.ProductDetails
import com.example.ecommerceapp.data.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductDetailsViewModel(private val repository: DatabaseRepository) : ViewModel() {
    private val _insertSuccess = MutableLiveData<Boolean>()
    fun insert(productDetails: ProductDetails) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.insertAllProducts(productDetails)
                withContext(Dispatchers.Main) {
                    _insertSuccess.value = true
                }
            }
        }
    }

    fun getAllProducts(): LiveData<List<ProductDetails>> {
        return repository.getAllProducts()
    }
}