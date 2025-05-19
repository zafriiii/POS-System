package com.example.possystemzw.viewmodel

import androidx.lifecycle.ViewModel
import com.example.possystemzw.model.Dish
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

    // List of dishes shown in UI
    private val _dishes = MutableStateFlow<List<Dish>>(emptyList())
    val dishes: StateFlow<List<Dish>> = _dishes.asStateFlow()

    // Dine-In / To-Go / Delivery selection
    private val _dineOption = MutableStateFlow("Dine In")
    val dineOption: StateFlow<String> = _dineOption.asStateFlow()

    init {
        fetchDishes() // load initially
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
                _dishes.value = emptyList() // or log error
            }
    }

    fun setDineOption(option: String) {
        _dineOption.value = option
    }

    fun addToCart(dish: Dish) {
        // Add dish to cart logic (to be implemented)
    }
}
