package com.example.ecommerceapp.adapter

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.databinding.ItemviewOrderedProductsBinding
import com.example.ecommerceapp.models.OrderedProductModel

class OrderedProductsRecyclerAdapter(
    private var orderedProductList: List<OrderedProductModel>
) : RecyclerView.Adapter<OrderedProductsRecyclerAdapter.ViewHolder>() {

    private var id: Int = 1

    class ViewHolder(val itemBinding: ItemviewOrderedProductsBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemviewOrderedProductsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderHistoryDetails = orderedProductList[position]
        holder.itemBinding.productName.text =
            id.toString() + ". " + orderHistoryDetails.productName + ". (" + orderHistoryDetails.productQuantity + ")"
        id++
        Log.d(ContentValues.TAG, "list2:$orderHistoryDetails")
    }

    override fun getItemCount(): Int {
        Log.d(ContentValues.TAG, "List size: ${orderedProductList.size}")
        return orderedProductList.size
    }
}