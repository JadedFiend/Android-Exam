package com.example.ecommerceapp.activities

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.AllProductsRecyclerAdapter
import com.example.ecommerceapp.add_to_cart.AddToCartViewModel
import com.example.ecommerceapp.commons.CommonMethods
import com.example.ecommerceapp.databinding.ActivityMainBinding
import com.example.ecommerceapp.favorite_product.FavoriteProductViewModel
import com.example.ecommerceapp.models.ProductDetails
import com.example.ecommerceapp.order_history.OrderHistoryViewModel
import com.example.ecommerceapp.product_details.ProductDetailsViewModel
import com.example.ecommerceapp.shared_prefs.PrefsManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import java.util.Locale
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var productDetailsViewModel: ProductDetailsViewModel
    private lateinit var addToCartViewModel: AddToCartViewModel
    private lateinit var favoriteProductViewModel: FavoriteProductViewModel
    private lateinit var orderHistoryViewModel: OrderHistoryViewModel

    private val commonMethods = CommonMethods()
    private val mPrefsManager: PrefsManager = PrefsManager()
    private lateinit var productList: List<ProductDetails>

    @SuppressLint("SetTextI18n")
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.appBar.textTitle.text = "Products"
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

        if (mPrefsManager.getInsertProductsOnce()) {
            Toast.makeText(this, "Data from API", Toast.LENGTH_LONG).show()
            if (isInternetAvailable()) {
                Toast.makeText(this, "Internet Available", Toast.LENGTH_LONG).show()
                binding.iconNoInternet.visibility = View.GONE
                GlobalScope.launch(Dispatchers.IO) {
                    val response = fetchDataFromApi()
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        Log.d("ApiWorker", "Response data: $responseData")

                        val jsonArray = JSONArray(responseData)

                        for (i in 0 until jsonArray.length()) {
                            val item = jsonArray.getJSONObject(i)
                            val itemId = item.getInt("id")
                            val itemTitle = item.getString("title")
                            val itemPrice = item.getString("price")
                            val itemDescription = item.getString("description")
                            val itemCategory = item.getString("category")
                            val itemImage = item.getString("image")

                            val ratingObject = item.getJSONObject("rating")
                            val itemRate = ratingObject.getString("rate")
                            val itemReview = ratingObject.getInt("count")
                            Log.d("ApiWorker", "Data for Item ID $itemId: $item")

                            // Insert a new product
                            val newProduct = ProductDetails(
                                productId = itemId,
                                productName = itemTitle,
                                productPrice = itemPrice,
                                productDescription = itemDescription,
                                productCategory = itemCategory,
                                productImage = itemImage,
                                productRating = itemRate,
                                productReviews = itemReview
                            )
                            productDetailsViewModel.insert(newProduct)
                        }
                        mPrefsManager.setInsertProductsOnce(false)
                    }
                }
            } else {
                Toast.makeText(this, "No internet", Toast.LENGTH_LONG).show()
                binding.iconNoInternet.visibility = View.VISIBLE
            }
        } else {
            Toast.makeText(this, "Data Already inserted", Toast.LENGTH_LONG).show()
        }

        productDetailsViewModel.getAllProducts().observe(this) { productsList ->
            Log.d(ContentValues.TAG, "all list:$productsList")
            productList = productsList
            favoriteProductViewModel.getFavoriteProduct().observe(this) { favoriteProductList ->
                binding.recyclerview.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                val adapter = AllProductsRecyclerAdapter(
                    this,
                    productsList,
                    emptyList(),
                    addToCartViewModel,
                    favoriteProductList,
                    favoriteProductViewModel,
                    "all_products"
                )
                binding.recyclerview.adapter = adapter
            }
        }

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

        binding.searchBar.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.cancel.visibility = View.VISIBLE
            } else {
                binding.cancel.visibility = View.GONE
            }
        }
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                search(editable.toString())
            }
        })
        binding.cancel.setOnClickListener { view ->
            binding.cancel.visibility = View.GONE
            val imm =
                this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            binding.searchBar.setText("")
            binding.searchBar.clearFocus()
        }

    }

    private suspend fun fetchDataFromApi(): Response {
        val client = OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url("https://fakestoreapi.com/products")
            .build()

        return withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            // for other device how are able to connect with Ethernet
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun search(string: String) {
        if (string == "") {
            productDetailsViewModel.getAllProducts().observe(this) { productsList ->
                productList = productsList
                favoriteProductViewModel.getFavoriteProduct().observe(this) { favoriteProductList ->
                    binding.recyclerview.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    val adapter = AllProductsRecyclerAdapter(
                        this,
                        productsList,
                        emptyList(),
                        addToCartViewModel,
                        favoriteProductList,
                        favoriteProductViewModel,
                        "all_products"
                    )
                    binding.recyclerview.adapter = adapter
                }
            }
        } else {
            val arrayList: ArrayList<ProductDetails> = ArrayList(productList)
            val searchResults = arrayList.filter {
                it.productName.lowercase(Locale.ROOT).contains(string.lowercase())
            }
            // Update the UI with the search results
            updateRecyclerView(searchResults)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateRecyclerView(searchResults: List<ProductDetails>) {
        binding.recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = AllProductsRecyclerAdapter(
            this,
            searchResults,
            emptyList(),
            addToCartViewModel,
            emptyList(),
            favoriteProductViewModel,
            "all_products"
        )
        binding.recyclerview.adapter = adapter
        adapter.notifyDataSetChanged()
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