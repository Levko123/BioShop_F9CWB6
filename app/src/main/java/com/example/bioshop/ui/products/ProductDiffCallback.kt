package com.example.bioshop.ui.products

import androidx.recyclerview.widget.DiffUtil
import com.example.bioshop.model.Product

class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(old: Product, new: Product) = old.id == new.id
    override fun areContentsTheSame(old: Product, new: Product) = old == new
}
