package com.example.possystemzw.viewmodel

import androidx.lifecycle.ViewModel
import com.example.possystemzw.model.Dish
import com.example.possystemzw.model.CartItem
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DishViewModel : ViewModel() {

    private val db = Firebase.firestore

    // Category filter
    private val _selectedCategory = MutableStateFlow("Hot Dishes")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Dish list
    private val _dishes = MutableStateFlow<List<Dish>>(emptyList())
    val dishes: StateFlow<List<Dish>> = _dishes.asStateFlow()

    // Dine-in / To-Go / Delivery
    private val _dineOption = MutableStateFlow("Dine In")
    val dineOption: StateFlow<String> = _dineOption.asStateFlow()

    // Cart
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    init {
        fetchDishes()
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
        fetchDishes()
    }

    private fun fetchDishes() {
        db.collection("dishes")
            .whereEqualTo("category", _selectedCategory.value)
            .get()
            .addOnSuccessListener { result ->
                val loadedDishes = result.map { it.toObject(Dish::class.java) }
                _dishes.value = loadedDishes
            }
            .addOnFailureListener {
                _dishes.value = emptyList()
            }
    }

    fun setDineOption(option: String) {
        _dineOption.value = option
    }

    fun addToCart(dish: Dish) {
        val currentCart = _cartItems.value.toMutableList()
        val existingItem = currentCart.find { it.dish.id == dish.id }

        if (existingItem != null) {
            existingItem.quantity += 1
        } else {
            currentCart.add(CartItem(dish))
        }

        _cartItems.value = currentCart
    }

    fun updateNote(dishId: String, note: String) {
        _cartItems.value = _cartItems.value.map {
            if (it.dish.id == dishId) it.copy(note = note) else it
        }.toList()
    }

    fun updateQuantity(dishId: String, qty: Int) {
        _cartItems.value = _cartItems.value.map {
            if (it.dish.id == dishId) it.copy(quantity = qty) else it
        }.toList()
    }

    fun removeFromCart(dishId: String) {
        _cartItems.value = _cartItems.value.filterNot { it.dish.id == dishId }
    }
    fun finalizeOrder(tableNumber: String, cashPaid: Double) {
        val orderData = hashMapOf(
            "dineOption" to dineOption.value,
            "tableNumber" to tableNumber,
            "cashPaid" to cashPaid,
            "subtotal" to cartItems.value.sumOf { it.quantity * it.dish.price },
            "timestamp" to System.currentTimeMillis(),
            "items" to cartItems.value.map {
                mapOf(
                    "name" to it.dish.name,
                    "price" to it.dish.price,
                    "quantity" to it.quantity,
                    "note" to it.note
                )
            }
        )

        Firebase.firestore.collection("orders")
            .add(orderData)
            .addOnSuccessListener {
                println("Order saved to Firestore")
                _cartItems.value = emptyList() // Optional: reset cart after order
            }
            .addOnFailureListener { e ->
                println("Failed to save order: ${e.message}")
            }
    }
}
