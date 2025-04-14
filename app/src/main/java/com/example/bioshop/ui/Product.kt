package com.example.bioshop

class Product {
    // Getterek és setterek
    var name: String? = null
    var description: String? = null
    var price: Int = 0

    // Konstruktor (üres is lehet a Firebase miatt)
    constructor()

    constructor(name: String?, description: String?, price: Int) {
        this.name = name
        this.description = description
        this.price = price
    }
}