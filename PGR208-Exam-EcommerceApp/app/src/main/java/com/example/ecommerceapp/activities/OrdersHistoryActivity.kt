package com.example.ecommerceapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.OrdersHistoryRecyclerAdapter
import com.example.ecommerceapp.add_to_cart.AddToCartViewModel
import com.example.ecommerceapp.commons.CommonMethods
import com.example.ecommerceapp.databinding.ActivityOrdersHistoryBinding
import com.example.ecommerceapp.favorite_product.FavoriteProductViewModel
import com.example.ecommerceapp.order_history.OrderHistoryViewModel
import com.example.ecommerceapp.product_details.ProductDetailsViewModel

class OrdersHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrdersHistoryBinding
    private lateinit var productDetailsViewModel: ProductDetailsViewModel
    private lateinit var addToCartViewModel: AddToCartViewModel
    private lateinit var favoriteProductViewModel: FavoriteProductViewModel
    private lateinit var orderHistoryViewModel: OrderHistoryViewModel

    private val commonMethods = CommonMethods()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersHistoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.appBar.textTitle.text = "Orders History"
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
            if (productsList.isNotEmpty()) {
                binding.appBar.orderAmountCard.visibility = View.VISIBLE
                binding.appBar.orderAmount.text = productsList.size.toString()
            } else {
                binding.appBar.orderAmountCard.visibility = View.GONE
                val layoutParams = binding.appBar.iconCart.layoutParams
                layoutParams.width = resources.getDimensionPixelSize(R.dimen.new_width)
                layoutParams.height = resources.getDimensionPixelSize(R.dimen.new_height)
            }
        }

        addToCartViewModel.getAddToCart().observe(this) { productsList ->
            if (productsList.isNotEmpty()) {
                binding.appBar.orderAmount.text = productsList.size.toString()
            } else {
                binding.appBar.orderAmountCard.visibility = View.GONE
                val layoutParams = binding.appBar.iconCart.layoutParams
                layoutParams.width = resources.getDimensionPixelSize(R.dimen.new_width)
                layoutParams.height = resources.getDimensionPixelSize(R.dimen.new_height)
            }
        }

        orderHistoryViewModel.getOrderHistory().observe(this) { productsList ->
            binding.recyclerview.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            val adapter = OrdersHistoryRecyclerAdapter(this, productsList)
            binding.recyclerview.adapter = adapter

        }

    }

    override fun onResume() {
        super.onResume()
        val viewModels = commonMethods.initializeViewModels(this)
        productDetailsViewModel = viewModels.productDetailsViewModel
        addToCartViewModel = viewModels.addToCartViewModel
        favoriteProductViewModel = viewModels.favoriteProductViewModel
        orderHistoryViewModel = viewModels.orderHistoryViewModel
        addToCartViewModel.getAddToCart().observe(this) { productsList ->
            if (productsList.isNotEmpty()) {
                binding.appBar.orderAmountCard.visibility = View.VISIBLE
                binding.appBar.orderAmount.text = productsList.size.toString()
            } else {
                binding.appBar.orderAmountCard.visibility = View.GONE
                val layoutParams = binding.appBar.iconCart.layoutParams
                layoutParams.width = resources.getDimensionPixelSize(R.dimen.new_width)
                layoutParams.height = resources.getDimensionPixelSize(R.dimen.new_height)
            }
        }
    }
}