package com.example.possystemzw.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.possystemzw.viewmodel.DishViewModel
import com.example.possystemzw.model.Dish

@Composable
fun HomeScreen(viewModel: DishViewModel = viewModel()) {
    val selectedCategory = viewModel.selectedCategory.collectAsState().value
    val dishes = viewModel.dishes.collectAsState().value
    val currentDineOption = viewModel.dineOption.collectAsState().value
    val options = listOf("Dine In", "To Go", "Delivery")

    Row(modifier = Modifier.fillMaxSize()) {

        // Left: Menu Section
        Column(
            modifier = Modifier
                .weight(1f)
                .background(Color(0xFF1F1D2B))
                .padding(16.dp)
        ) {
            // App title and date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Jaegar Resto", fontSize = 20.sp, color = Color.White)
                Text("Tue, 2 Feb 2021", fontSize = 12.sp, color = Color.White.copy(alpha = 0.6f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Category Tabs
            CategoryTabs(
                selected = selectedCategory,
                onSelect = { viewModel.selectCategory(it) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Dine Option Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                options.forEach { option ->
                    val isSelected = option == currentDineOption
                    Button(
                        onClick = { viewModel.setDineOption(option) },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (isSelected) Color(0xFFFF7E00) else Color(0xFF252836)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    ) {
                        Text(option, color = Color.White, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Dish Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(dishes) { dish: Dish ->
                    DishCard(dish = dish, onAddToCart = { viewModel.addToCart(it) })
                }
            }
        }

        // Right: Cart Panel
        CartScreen(viewModel = viewModel)
    }
}
