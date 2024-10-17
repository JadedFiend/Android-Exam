package com.example.ecommerceapp.adapter

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.databinding.ItemviewOrderHistoryBinding
import com.example.ecommerceapp.models.OrderHistoryModel

class OrdersHistoryRecyclerAdapter(
    private val context: Context,
    private var orderHistoryList: List<OrderHistoryModel>
) : RecyclerView.Adapter<OrdersHistoryRecyclerAdapter.ViewHolder>() {

    class ViewHolder(val itemBinding: ItemviewOrderHistoryBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemviewOrderHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderHistoryDetails = orderHistoryList[position]
        holder.itemBinding.orderDateTime.text = orderHistoryDetails.orderTime


        holder.itemBinding.innerRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val adapter = OrderedProductsRecyclerAdapter(orderHistoryDetails.orderedProductList)
        Log.d(TAG, "list:" + orderHistoryDetails.orderedProductList)
        holder.itemBinding.innerRecyclerView.adapter = adapter

        holder.itemBinding.orderAmount.text =
            "$" + orderHistoryDetails.orderAmount + " (" + orderHistoryDetails.orderedProductQuantity + " items)"
    }

    override fun getItemCount(): Int {
        return orderHistoryList.size
    }
}