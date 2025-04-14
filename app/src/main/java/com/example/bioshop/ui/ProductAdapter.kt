package com.example.bioshop

import android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(private val products: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // item layout
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.simple_list_item_2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = products[position]
        holder.text1.text = current.name
        holder.text2.text = current.description + " - " + current.price + " Ft"
    }

    override fun getItemCount(): Int {
        return products.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var text1: TextView = itemView.findViewById(R.id.text1)
        var text2: TextView = itemView.findViewById(R.id.text2)
    }
}