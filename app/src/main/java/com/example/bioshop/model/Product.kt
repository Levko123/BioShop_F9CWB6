package com.example.bioshop.model
data class Product(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val inStock: Boolean = true
)


