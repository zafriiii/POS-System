package com.example.possystemzw.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.possystemzw.model.Dish

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DishCard(
    dish: Dish,
    onAddToCart: (Dish) -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color(0xFF252836)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlideImage(
                model = dish.imageUrl,
                contentDescription = dish.name,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = dish.name,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "$${"%.2f".format(dish.price)}",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp
            )

            Text(
                text = "${dish.stock} Bowls available",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 10.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Button(
                onClick = { onAddToCart(dish) },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFF7E00)),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text("Add", color = Color.Black, fontSize = 12.sp)
            }
        }
    }
}
