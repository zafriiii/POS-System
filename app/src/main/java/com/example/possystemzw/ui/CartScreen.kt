package com.example.possystemzw.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.possystemzw.model.CartItem
import com.example.possystemzw.viewmodel.DishViewModel

@Composable
fun CartScreen(viewModel: DishViewModel) {
    val cartItems = viewModel.cartItems.collectAsState().value
    val subtotal = cartItems.sumOf { it.quantity * it.dish.price }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp)
            .background(Color(0xFF252836))
            .padding(12.dp)
    ) {
        Text("Orders", fontSize = 18.sp, color = Color.White)

        Spacer(Modifier.height(8.dp))

        cartItems.forEach { item ->
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(item.dish.name, color = Color.White)
                    Text("x${item.quantity}", color = Color.White)

                    IconButton(onClick = {
                        viewModel.removeFromCart(item.dish.id)
                    }) {
                        Icon(Icons.Filled.Delete, contentDescription = null, tint = Color.Red)
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Note:", color = Color.Gray, fontSize = 12.sp)
                    Spacer(Modifier.width(4.dp))

                    var noteState by remember { mutableStateOf(TextFieldValue(item.note)) }

                    TextField(
                        value = noteState,
                        onValueChange = {
                            noteState = it
                            viewModel.updateNote(item.dish.id, it.text)
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            textColor = Color.White,
                            cursorColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                    )
                }

                Spacer(Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Subtotal
        Text("Subtotal: $${"%.2f".format(subtotal)}", fontSize = 16.sp, color = Color.White)

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { /* navigate to payment screen */ },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFF7E00)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue to Payment", color = Color.Black)
        }
    }
}
