package com.example.malesheep.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.malesheep.data.model.StockEntity
import com.example.malesheep.ui.components.AppHeader
import com.example.malesheep.ui.components.formatMoney
import com.example.malesheep.ui.theme.*
import com.example.malesheep.ui.viewmodel.SheepViewModel

@Composable
fun StockScreen(
    viewModel: SheepViewModel,
    onBack: () -> Unit
) {
    val stockList by viewModel.stock.collectAsStateWithLifecycle()

    var name by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(viewModel.getTodayString()) }
    var price by remember { mutableStateOf("") }
    var transport by remember { mutableStateOf("") }
    var load by remember { mutableStateOf("") }
    var qty by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val autoAvgPrice = remember(price, transport, load, qty) {
        val p = price.toDoubleOrNull() ?: 0.0
        val t = transport.toDoubleOrNull() ?: 0.0
        val l = load.toDoubleOrNull() ?: 0.0
        val q = qty.toDoubleOrNull() ?: 0.0
        if (q > 0) {
            (p + t + l) / q
        } else {
            0.0
        }
    }

    Scaffold(
        topBar = {
            AppHeader(title = "🌾 தீவனம் வாங்கிய பதிவு", onBack = onBack)
        },
        containerColor = Background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Form Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "🌾 புதிய தீவனம் இருப்பு சேர்க்கை",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Green800
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("தீவனம் பெயர்") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Green800,
                                    unfocusedBorderColor = Border
                                )
                            )
                            OutlinedTextField(
                                value = date,
                                onValueChange = { date = it },
                                label = { Text("வாங்கிய தேதி") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Green800,
                                    unfocusedBorderColor = Border
                                )
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = price,
                                onValueChange = { price = it },
                                label = { Text("விலை (₹)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Green800,
                                    unfocusedBorderColor = Border
                                )
                            )
                            OutlinedTextField(
                                value = transport,
                                onValueChange = { transport = it },
                                label = { Text("வண்டி வாடகை (₹)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Green800,
                                    unfocusedBorderColor = Border
                                )
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = load,
                                onValueChange = { load = it },
                                label = { Text("இறக்கு கூலி (₹)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Green800,
                                    unfocusedBorderColor = Border
                                )
                            )
                            OutlinedTextField(
                                value = qty,
                                onValueChange = { qty = it },
                                label = { Text("அளவு (kg)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Green800,
                                    unfocusedBorderColor = Border
                                )
                            )
                        }

                        OutlinedTextField(
                            value = if (autoAvgPrice > 0) formatMoney(autoAvgPrice) + " / kg" else "-",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("சராசரி தீவன விலை (₹/kg) — Auto") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Green800,
                                unfocusedBorderColor = Border
                            )
                        )

                        if (errorMessage.isNotBlank()) {
                            Text(errorMessage, color = Red700, fontSize = 13.sp)
                        }

                        Button(
                            onClick = {
                                if (name.isBlank() || qty.isBlank()) {
                                    errorMessage = "தயவுசெய்து தீவனம் பெயர் மற்றும் அளவை உள்ளிடவும்."
                                    return@Button
                                }
                                val p = price.toDoubleOrNull() ?: 0.0
                                val t = transport.toDoubleOrNull() ?: 0.0
                                val l = load.toDoubleOrNull() ?: 0.0
                                val q = qty.toDoubleOrNull() ?: 0.0

                                viewModel.addStock(
                                    StockEntity(
                                        name = name.trim(),
                                        date = date,
                                        price = p,
                                        transport = t,
                                        load = l,
                                        qty = q,
                                        avg = autoAvgPrice
                                    )
                                ) {
                                    name = ""
                                    price = ""
                                    transport = ""
                                    load = ""
                                    qty = ""
                                    errorMessage = ""
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Green800)
                        ) {
                            Text("இருப்பு பதிவு சேமிக்க", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Stock Records List
            item {
                Text(
                    text = "தீவனம் இருப்புப் பட்டியல் (${stockList.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Slate800,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            if (stockList.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Text(
                            text = "பதிவுகள் எதுவும் இல்லை",
                            fontSize = 14.sp,
                            color = Slate500,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                items(stockList, key = { it.id }) { item ->
                    val total = item.price + item.transport + item.load
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = item.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = Green800
                                    )
                                    Text(
                                        text = "(${item.date})",
                                        fontSize = 12.sp,
                                        color = Slate500
                                    )
                                }
                                Text(
                                    text = "அளவு: ${item.qty} kg | விலை: ${formatMoney(item.avg)}/kg",
                                    fontSize = 13.sp,
                                    color = Slate700
                                )
                                Text(
                                    text = "மொத்த அடக்க விலை: ${formatMoney(total)}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Green700
                                )
                            }

                            IconButton(onClick = { viewModel.deleteStock(item.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Stock",
                                    tint = Red700
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
