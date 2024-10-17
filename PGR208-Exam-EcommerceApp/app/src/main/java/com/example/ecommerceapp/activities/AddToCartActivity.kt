package com.example.ecommerceapp.activities

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.AllProductsRecyclerAdapter
import com.example.ecommerceapp.add_to_cart.AddToCartViewModel
import com.example.ecommerceapp.commons.CommonMethods
import com.example.ecommerceapp.databinding.ActivityAddToCartBinding
import com.example.ecommerceapp.favorite_product.FavoriteProductViewModel
import com.example.ecommerceapp.models.AddToCartModel
import com.example.ecommerceapp.models.OrderHistoryModel
import com.example.ecommerceapp.models.OrderedProductModel
import com.example.ecommerceapp.order_history.OrderHistoryViewModel
import com.example.ecommerceapp.product_details.ProductDetailsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddToCartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddToCartBinding
    private lateinit var productDetailsViewModel: ProductDetailsViewModel
    private lateinit var addToCartViewModel: AddToCartViewModel
    private lateinit var favoriteProductViewModel: FavoriteProductViewModel
    private lateinit var orderHistoryViewModel: OrderHistoryViewModel

    private val commonMethods = CommonMethods()
    private var cartProductList: List<AddToCartModel> = listOf()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddToCartBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.appBar.textTitle.text = "Cart"
        binding.appBar.iconBack.visibility = View.VISIBLE
        binding.appBar.iconBack.setOnClickListener {
            onBackPressed()
        }
        binding.appBar.iconCart.setOnClickListener {
            val intent = Intent(this, AddToCartActivity::class.java)
            startActivity(intent)
        }
        binding.appBar.iconFavorite.setOnClickListener {
            val intent = Intent(this, FavoriteProductActivity::class.java)
            startActivity(intent)
        }
        binding.appBar.iconHistory.setOnClickListener {
            val intent = Intent(this, OrdersHistoryActivity::class.java)
            startActivity(intent)
        }

        val viewModels = commonMethods.initializeViewModels(this)
        productDetailsViewModel = viewModels.productDetailsViewModel
        addToCartViewModel = viewModels.addToCartViewModel
        favoriteProductViewModel = viewModels.favoriteProductViewModel
        orderHistoryViewModel = viewModels.orderHistoryViewModel

        addToCartViewModel.getAddToCart().observe(this) { productsList ->
            Log.d(TAG, "cort list:$productsList")

            if (productsList.isNotEmpty()) {
                binding.appBar.orderAmountCard.visibility = View.VISIBLE
                binding.appBar.orderAmount.text = productsList.size.toString()
            } else {
                binding.appBar.orderAmountCard.visibility = View.GONE
                val layoutParams = binding.appBar.iconCart.layoutParams
                layoutParams.width = resources.getDimensionPixelSize(R.dimen.new_width)
                layoutParams.height = resources.getDimensionPixelSize(R.dimen.new_height)
            }

            var amount: Float = 0.0F
            for (items in productsList) {
                val productPrice = items.productQuantity * items.productPrice.toFloat()
                // val product_price = items.productPrice.toFloat()
                amount += productPrice
            }
            binding.placeOrder.text = "Place Order ($$amount)"

            cartProductList = productsList

            binding.recyclerview.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            val adapter = AllProductsRecyclerAdapter(
                this,
                emptyList(),
                productsList,
                addToCartViewModel,
                emptyList(),
                favoriteProductViewModel,
                "cart_products"
            )
            binding.recyclerview.adapter = adapter
        }

        binding.placeOrder.setOnClickListener {
            //   addToCartViewModel.getAddToCart().observe(this) {productsList ->
            val orderedProductList: MutableList<OrderedProductModel> = mutableListOf()
            var amount: Float = 0.0F
            for (item in cartProductList) {
                val productPrice = item.productPrice.toFloat()
                amount += productPrice

                val productId = item.productId
                val productName = item.productName
                val productQuantity = item.productQuantity
                val orderedProduct = OrderedProductModel(productId, productName, productQuantity)
                orderedProductList.add(orderedProduct)
            }

            val orderTime: Date = Date()
            val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT' Z yyyy", Locale.US)
            val formattedDate: String = dateFormat.format(orderTime)

            val newOrder = OrderHistoryModel(
                orderTime = formattedDate,
                orderAmount = amount.toString(),
                orderedProductQuantity = orderedProductList.size,
                orderedProductList = orderedProductList
            )

            orderHistoryViewModel.insertOrderHistory(newOrder)

            addToCartViewModel.getAddToCart().removeObservers(this)
            for (item in cartProductList) {
                addToCartViewModel.deleteFromCart(item)
            }
        }

    }
}