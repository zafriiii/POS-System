package com.example.possystemzw.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CategoryTabs(
    selected: String,
    onSelect: (String) -> Unit
) {
    val categories = listOf("Hot Dishes", "Cold Dishes", "Soup", "Grill", "Appetizer", "Dessert")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            val isSelected = category == selected

            Button(
                onClick = { onSelect(category) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isSelected) Color(0xFFFF7E00) else Color(0xFF252836)
                ),
                shape = MaterialTheme.shapes.small,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = category,
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}
