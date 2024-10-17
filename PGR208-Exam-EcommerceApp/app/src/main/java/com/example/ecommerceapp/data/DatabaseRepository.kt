package com.example.ecommerceapp.data

import androidx.lifecycle.LiveData
import com.example.ecommerceapp.add_to_cart.AddToCartDao
import com.example.ecommerceapp.favorite_product.FavoriteProductDao
import com.example.ecommerceapp.models.AddToCartModel
import com.example.ecommerceapp.models.FavoriteModel
import com.example.ecommerceapp.models.OrderHistoryModel
import com.example.ecommerceapp.models.ProductDetails
import com.example.ecommerceapp.order_history.OrderHistoryDao
import com.example.ecommerceapp.product_details.ProductDetailsDao

class DatabaseRepository(private val productDetailsDao: ProductDetailsDao, private val addToCartDao: AddToCartDao, private val favoriteProductDao: FavoriteProductDao, private val orderHistoryDao: OrderHistoryDao) {

    suspend fun insertAllProducts(productDetails: ProductDetails) {
        productDetailsDao.insertAllProducts(productDetails)
    }

    fun getAllProducts(): LiveData<List<ProductDetails>> {
        return productDetailsDao.getAllProducts()
    }

    suspend fun insertAddToCart(addToCartModel: AddToCartModel) {
        addToCartDao.insertAddToCart(addToCartModel)
    }

    fun getAddToCart(): LiveData<List<AddToCartModel>> {
        return addToCartDao.getAddToCart()
    }

    suspend fun deleteFromCart(addToCartModel: AddToCartModel) {
        addToCartDao.deleteFromCart(addToCartModel)
    }

    suspend fun updateProductQuantity(addToCartModel: AddToCartModel) {
        addToCartDao.updateProductQuantity(addToCartModel)
    }

    suspend fun insertFavoriteProduct(favoriteModel: FavoriteModel) {
        favoriteProductDao.insertFavoriteProduct(favoriteModel)
    }

    fun getFavoriteProduct(): LiveData<List<FavoriteModel>> {
        return favoriteProductDao.getFavoriteProduct()
    }

    suspend fun deleteFavoriteProduct(favoriteModel: FavoriteModel) {
        favoriteProductDao.deleteFavoriteProduct(favoriteModel)
    }

    suspend fun insertOrderHistory(orderHistoryModel: OrderHistoryModel) {
        orderHistoryDao.insertOrderHistory(orderHistoryModel)
    }

    fun getOrderHistory(): LiveData<List<OrderHistoryModel>> {
        return orderHistoryDao.getOrderHistory()
    }

}