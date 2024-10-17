package com.example.ecommerceapp.add_to_cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.models.AddToCartModel
import com.example.ecommerceapp.data.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddToCartViewModel(private val repository: DatabaseRepository) : ViewModel() {
    private val _insertSuccess = MutableLiveData<Boolean>()
    fun insertAddToCart(addToCartModel: AddToCartModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.insertAddToCart(addToCartModel)
                withContext(Dispatchers.Main) {
                    _insertSuccess.value = true
                }
            }
        }
    }

    fun getAddToCart(): LiveData<List<AddToCartModel>> {
        return repository.getAddToCart()
    }

    fun deleteFromCart(addToCartModel: AddToCartModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteFromCart(addToCartModel)
            }
        }
    }

    fun updateProductQuantity(addToCartModel: AddToCartModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.updateProductQuantity(addToCartModel)
            }
        }
    }
}