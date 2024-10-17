package com.example.ecommerceapp.adapter

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerceapp.activities.ProductDetailsActivity
import com.example.ecommerceapp.add_to_cart.AddToCartViewModel
import com.example.ecommerceapp.databinding.ItemviewAllproductsRecyclerviewBinding
import com.example.ecommerceapp.favorite_product.FavoriteProductViewModel
import com.example.ecommerceapp.models.AddToCartModel
import com.example.ecommerceapp.models.FavoriteModel
import com.example.ecommerceapp.models.ProductDetails
import java.util.Locale

class AllProductsRecyclerAdapter(
    private val context: Context,
    private var allProductsList: List<ProductDetails>,
    private var cartProductsList: List<AddToCartModel>,
    private val addToCartViewModel: AddToCartViewModel,
    private var favoriteProductsList: List<FavoriteModel>,
    private val favoriteProductViewModel: FavoriteProductViewModel,
    private val textId: String
) : RecyclerView.Adapter<AllProductsRecyclerAdapter.ViewHolder>() {
    class ViewHolder(val itemBinding: ItemviewAllproductsRecyclerviewBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemviewAllproductsRecyclerviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(itemBinding)
    }

    @SuppressLint("DiscouragedApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (textId == "all_products" && position < allProductsList.size) {
            val allProductDetails = allProductsList[position]

            holder.itemBinding.linearLayout.weightSum = 5.0f
            (holder.itemBinding.linearLayout.getChildAt(0).layoutParams as LinearLayout.LayoutParams).weight =
                5.0f
            (holder.itemBinding.linearLayout.getChildAt(1).layoutParams as LinearLayout.LayoutParams).weight =
                0.0f

            holder.itemBinding.quantityLayout.visibility = View.GONE
            holder.itemBinding.productCatagory.visibility = View.GONE
            holder.itemBinding.relativeLayout.visibility = View.GONE
            Glide.with(context)
                .load(allProductDetails.productImage)
                .into(holder.itemBinding.productImage)
            holder.itemBinding.productName.text = allProductDetails.productName

            val formattedPrice =
                String.format(Locale.getDefault(), "$%s", allProductDetails.productPrice)
            holder.itemBinding.productPrice.text = formattedPrice
            Log.d(ContentValues.TAG, "productReview:" + allProductDetails.productReviews)

            holder.itemBinding.iconFavorite.visibility = View.VISIBLE
            val isFavorite =
                favoriteProductsList.any { it.productId == allProductDetails.productId }
            if (isFavorite) {
                val drawableResourceId: Int = context.resources.getIdentifier(
                    "baseline_favorite_24",
                    "drawable",
                    context.packageName
                )
                holder.itemBinding.iconFavorite.setImageResource(drawableResourceId)

            } else {
                val drawableResourceId: Int = context.resources.getIdentifier(
                    "baseline_favorite_border_24",
                    "drawable",
                    context.packageName
                )
                holder.itemBinding.iconFavorite.setImageResource(drawableResourceId)
            }
        } else if (textId == "cart_products" && position < cartProductsList.size) {
            val cartProductDetails = cartProductsList[position]

            holder.itemBinding.linearLayout.weightSum = 5.0f
            (holder.itemBinding.linearLayout.getChildAt(0).layoutParams as LinearLayout.LayoutParams).weight =
                4.5f
            (holder.itemBinding.linearLayout.getChildAt(1).layoutParams as LinearLayout.LayoutParams).weight =
                0.5f

            holder.itemBinding.quantityLayout.visibility = View.VISIBLE
            holder.itemBinding.iconFavorite.visibility = View.GONE
            Glide.with(context)
                .load(cartProductDetails.productImage)
                .into(holder.itemBinding.productImage)
            holder.itemBinding.productName.text = cartProductDetails.productName
            val formattedPrice =
                String.format(Locale.getDefault(), "$%s", cartProductDetails.productPrice)
            holder.itemBinding.productPrice.text = formattedPrice
            holder.itemBinding.productCatagory.text = cartProductDetails.productCategory
            holder.itemBinding.amountOfItems.text = cartProductDetails.productQuantity.toString()

            holder.itemBinding.minus.setOnClickListener {
                if (cartProductDetails.productQuantity > 1) {
                    cartProductDetails.productQuantity--
                    holder.itemBinding.amountOfItems.text =
                        cartProductDetails.productQuantity.toString()
                    addToCartViewModel.updateProductQuantity(cartProductDetails)
                }
            }
            holder.itemBinding.plus.setOnClickListener {
                cartProductDetails.productQuantity++
                holder.itemBinding.amountOfItems.text =
                    cartProductDetails.productQuantity.toString()
                addToCartViewModel.updateProductQuantity(cartProductDetails)
            }

        } else if (textId == "favorite_products") {
            val favoriteProductDetails = favoriteProductsList[position]

            holder.itemBinding.linearLayout.weightSum = 5.0f
            (holder.itemBinding.linearLayout.getChildAt(0).layoutParams as LinearLayout.LayoutParams).weight =
                5.0f
            (holder.itemBinding.linearLayout.getChildAt(1).layoutParams as LinearLayout.LayoutParams).weight =
                0.0f

            holder.itemBinding.quantityLayout.visibility = View.GONE
            holder.itemBinding.productCatagory.visibility = View.VISIBLE
            holder.itemBinding.relativeLayout.visibility = View.GONE

            Glide.with(context)
                .load(favoriteProductDetails.productImage)
                .into(holder.itemBinding.productImage)

            val drawableResourceId: Int = context.resources.getIdentifier(
                "baseline_favorite_24",
                "drawable",
                context.packageName
            )
            holder.itemBinding.iconFavorite.setImageResource(drawableResourceId)

            holder.itemBinding.productName.text = favoriteProductDetails.productName
            holder.itemBinding.productCatagory.text = favoriteProductDetails.productCategory

            val formattedPrice =
                String.format(Locale.getDefault(), "$%s", favoriteProductDetails.productPrice)
            holder.itemBinding.productPrice.text = formattedPrice
        }

        holder.itemBinding.iconCross.setOnClickListener {
            if (textId == "cart_products") {
                val cartProductDetails = cartProductsList[position]
                addToCartViewModel.deleteFromCart(cartProductDetails)
            }
        }

        holder.itemBinding.iconFavorite.setOnClickListener {
            if (textId == "all_products") {
                val allProductDetails = allProductsList[position]

                // Check if the product is already in the favorites list
                val isFavorite =
                    favoriteProductsList.any { it.productId == allProductDetails.productId }

                if (isFavorite) {
                    // If it's already a favorite, remove it
                    val favoriteProductDetails =
                        favoriteProductsList.firstOrNull { it.productId == allProductDetails.productId }
                    if (favoriteProductDetails != null) {
                        favoriteProductViewModel.deleteFavoriteProduct(favoriteProductDetails)
                        val drawableResourceId: Int = context.resources.getIdentifier(
                            "baseline_favorite_border_24",
                            "drawable",
                            context.packageName
                        )
                        holder.itemBinding.iconFavorite.setImageResource(drawableResourceId)
                    }
                } else {
                    // If it's not a favorite, add it
                    val newProduct = FavoriteModel(
                        productId = allProductDetails.productId,
                        productName = allProductDetails.productName,
                        productPrice = allProductDetails.productPrice,
                        productDescription = allProductDetails.productDescription,
                        productCategory = allProductDetails.productCategory,
                        productImage = allProductDetails.productImage,
                        productRating = allProductDetails.productRating,
                        productReviews = allProductDetails.productReviews
                    )
                    favoriteProductViewModel.insertFavoriteProduct(newProduct)
                }
            } else if (textId == "favorite_products") {
                val favoriteProductDetails = favoriteProductsList[position]

                favoriteProductViewModel.deleteFavoriteProduct(favoriteProductDetails)
            }
        }

        holder.itemBinding.card.setOnClickListener {
            if (textId == "all_products") {
                val allProductDetails = allProductsList[position]
                val intent = Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra("productId", allProductDetails.productId)
                intent.putExtra("productImage", allProductDetails.productImage)
                intent.putExtra("productName", allProductDetails.productName)
                intent.putExtra("productPrice", allProductDetails.productPrice)
                intent.putExtra("productCategory", allProductDetails.productCategory)
                intent.putExtra("productRate", allProductDetails.productRating)
                intent.putExtra("productReview", allProductDetails.productReviews)
                intent.putExtra("productDetail", allProductDetails.productDescription)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return when (textId) {
            "all_products" -> allProductsList.size
            "favorite_products" -> favoriteProductsList.size
            else -> cartProductsList.size
        }
    }
}