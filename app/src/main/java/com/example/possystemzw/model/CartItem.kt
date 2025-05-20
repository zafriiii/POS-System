package com.example.possystemzw.model

data class CartItem(
    val dish: Dish,
    var quantity: Int = 1,
    var note: String = ""
)
