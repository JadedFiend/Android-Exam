package com.example.ecommerceapp.favorite_product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.models.FavoriteModel
import com.example.ecommerceapp.data.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteProductViewModel(private val repository: DatabaseRepository) : ViewModel() {
    private val _insertSuccess = MutableLiveData<Boolean>()
    fun insertFavoriteProduct(favoriteModel: FavoriteModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.insertFavoriteProduct(favoriteModel)
                withContext(Dispatchers.Main) {
                    _insertSuccess.value = true
                }
            }
        }
    }

    fun getFavoriteProduct(): LiveData<List<FavoriteModel>> {
        return repository.getFavoriteProduct()
    }

    fun deleteFavoriteProduct(favoriteModel: FavoriteModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteFavoriteProduct(favoriteModel)
            }
        }
    }
}