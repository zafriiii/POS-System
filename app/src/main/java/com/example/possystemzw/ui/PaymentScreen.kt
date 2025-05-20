package com.example.possystemzw.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.possystemzw.viewmodel.DishViewModel

@Composable
fun PaymentScreen(
    viewModel: DishViewModel,
    onConfirm: () -> Unit,
    onBack: () -> Unit
) {
    val cartItems = viewModel.cartItems.collectAsState().value
    val dineOption = viewModel.dineOption.collectAsState().value
    val subtotal = cartItems.sumOf { it.quantity * it.dish.price }

    var tableNumber by remember { mutableStateOf("") }
    var cashPaid by remember { mutableStateOf("") }

    val showTableField = dineOption == "Dine In"
    val isCashValid = cashPaid.toDoubleOrNull()?.let { it >= subtotal } == true
    val isTableValid = if (showTableField) tableNumber.isNotBlank() else true
    val change = if (cashPaid.toDoubleOrNull() != null)
        cashPaid.toDouble() - subtotal else 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .background(Color(0xFF1F1D2B)),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Review & Payment", color = Color.White, fontSize = 20.sp)
        Text("Order Type: $dineOption", color = Color.White)

        cartItems.forEach {
            Text("${it.quantity}x ${it.dish.name}", color = Color.White, fontSize = 14.sp)
        }

        Text("Subtotal: $${"%.2f".format(subtotal)}", color = Color.White)

        if (showTableField) {
            OutlinedTextField(
                value = tableNumber,
                onValueChange = { tableNumber = it },
                label = { Text("Table Number") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.White
                )
            )
        }

        OutlinedTextField(
            value = cashPaid,
            onValueChange = { input ->
                // Accept only digits and dot
                cashPaid = input.filter { it.isDigit() || it == '.' }
            },
            label = { Text("Cash Paid") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = VisualTransformation.None,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.White
            )
        )

        if (cashPaid.isNotEmpty()) {
            Text(
                if (change >= 0) "Change: $${"%.2f".format(change)}"
                else "Insufficient payment",
                color = if (change < 0) Color.Red else Color.White
            )
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.finalizeOrder(tableNumber, cashPaid.toDouble())
                onConfirm()
            },
            enabled = isCashValid && isTableValid,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFF7E00)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirm Payment", color = Color.Black)
        }

        TextButton(onClick = onBack) {
            Text("Back", color = Color.White)
        }
    }
}
