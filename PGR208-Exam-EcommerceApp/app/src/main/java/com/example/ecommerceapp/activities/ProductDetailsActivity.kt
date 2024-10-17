package com.example.ecommerceapp.activities

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.example.ecommerceapp.R
import com.example.ecommerceapp.add_to_cart.AddToCartViewModel
import com.example.ecommerceapp.commons.CommonMethods
import com.example.ecommerceapp.databinding.ActivityProductDetailsBinding
import com.example.ecommerceapp.favorite_product.FavoriteProductViewModel
import com.example.ecommerceapp.models.AddToCartModel
import com.example.ecommerceapp.order_history.OrderHistoryViewModel
import com.example.ecommerceapp.product_details.ProductDetailsViewModel

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var productDetailsViewModel: ProductDetailsViewModel
    private lateinit var addToCartViewModel: AddToCartViewModel
    private lateinit var favoriteProductViewModel: FavoriteProductViewModel
    private lateinit var orderHistoryViewModel: OrderHistoryViewModel

    private val commonMethods = CommonMethods()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.appBar.textTitle.text = "Product Details"
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

        val productId = intent.getIntExtra("productId", 0)
        val productImage = intent.getStringExtra("productImage")
        val productName = intent.getStringExtra("productName")
        val productPrice = intent.getStringExtra("productPrice")
        val productCategory = intent.getStringExtra("productCategory")
        val productRate = intent.getStringExtra("productRate")
        val productReview = intent.getIntExtra("productReview", 0)
        val productDetail = intent.getStringExtra("productDetail")

        Log.d(TAG, "productReview:$productReview")

        binding.productName.text = productName
        binding.productCategory.text = productCategory
        binding.productRate.text = productRate
        val stringReview = " ($productReview)"
        binding.productReviews.text = stringReview
        binding.productDetail.text = productDetail

        Glide.with(this)
            .load(productImage)
            .into(binding.productImage)

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

        binding.addToCart.setOnClickListener {
            val newAddToCart = AddToCartModel(
                productId = productId,
                productName = productName.toString(),
                productPrice = productPrice.toString(),
                productCategory = productCategory.toString(),
                productImage = productImage.toString(),
                productQuantity = 1
            )
            addToCartViewModel.insertAddToCart(newAddToCart)

            val intent = Intent(this, AddToCartActivity::class.java)
            startActivity(intent)
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