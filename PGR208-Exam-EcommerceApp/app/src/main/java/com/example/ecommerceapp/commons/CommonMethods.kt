package com.example.ecommerceapp.commons

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.ecommerceapp.add_to_cart.AddToCartViewModel
import com.example.ecommerceapp.favorite_product.FavoriteProductViewModel
import com.example.ecommerceapp.order_history.OrderHistoryViewModel
import com.example.ecommerceapp.product_details.ProductDetailsViewModel
import com.example.ecommerceapp.data.DatabaseClient
import com.example.ecommerceapp.data.DatabaseRepository
import com.example.ecommerceapp.data.ViewModelFactory

class CommonMethods {
    fun initializeViewModels(context: FragmentActivity): ViewModelContainer {
        val appDatabase = DatabaseClient.getInstance(context)
        val productDetailsDao = appDatabase.productDetailsDao()
        val addToCartDao = appDatabase.addToCartDao()
        val favoriteProductDao = appDatabase.favoriteProductDao()
        val orderHistoryDao = appDatabase.orderHistoryDao()
        val repository =
            DatabaseRepository(productDetailsDao, addToCartDao, favoriteProductDao, orderHistoryDao)
        val productDetailsViewModel = ViewModelProvider(
            context,
            ViewModelFactory(repository)
        )[ProductDetailsViewModel::class.java]
        val addToCartViewModel =
            ViewModelProvider(context, ViewModelFactory(repository))[AddToCartViewModel::class.java]
        val favoriteProductViewModel = ViewModelProvider(
            context,
            ViewModelFactory(repository)
        )[FavoriteProductViewModel::class.java]
        val orderHistoryViewModel = ViewModelProvider(
            context,
            ViewModelFactory(repository)
        )[OrderHistoryViewModel::class.java]

        return ViewModelContainer(
            productDetailsViewModel,
            addToCartViewModel,
            favoriteProductViewModel,
            orderHistoryViewModel
        )
    }
}

data class ViewModelContainer(
    val productDetailsViewModel: ProductDetailsViewModel,
    val addToCartViewModel: AddToCartViewModel,
    val favoriteProductViewModel: FavoriteProductViewModel,
    val orderHistoryViewModel: OrderHistoryViewModel
)