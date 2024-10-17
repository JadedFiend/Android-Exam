package com.example.ecommerceapp.activities

import android.content.ContentValues
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
import com.example.ecommerceapp.databinding.ActivityFavoriteProductBinding
import com.example.ecommerceapp.favorite_product.FavoriteProductViewModel
import com.example.ecommerceapp.order_history.OrderHistoryViewModel
import com.example.ecommerceapp.product_details.ProductDetailsViewModel

class FavoriteProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteProductBinding
    private lateinit var productDetailsViewModel: ProductDetailsViewModel
    private lateinit var addToCartViewModel: AddToCartViewModel
    private lateinit var favoriteProductViewModel: FavoriteProductViewModel
    private lateinit var orderHistoryViewModel: OrderHistoryViewModel

    private val commonMethods = CommonMethods()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteProductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.appBar.textTitle.text = "Favorite Products"
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

        favoriteProductViewModel.getFavoriteProduct().observe(this) { productsList ->
            Log.d(ContentValues.TAG, "cort list:$productsList")
            binding.recyclerview.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            val adapter = AllProductsRecyclerAdapter(
                this,
                emptyList(),
                emptyList(),
                addToCartViewModel,
                productsList,
                favoriteProductViewModel,
                "favorite_products"
            )
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