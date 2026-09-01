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
import com.example.malesheep.data.model.SalesEntity
import com.example.malesheep.ui.components.AppHeader
import com.example.malesheep.ui.components.CustomDropdownField
import com.example.malesheep.ui.components.formatMoney
import com.example.malesheep.ui.theme.*
import com.example.malesheep.ui.viewmodel.SheepViewModel

@Composable
fun SalesScreen(
    viewModel: SheepViewModel,
    onBack: () -> Unit
) {
    val salesList by viewModel.sales.collectAsStateWithLifecycle()
    val sheepList by viewModel.sheep.collectAsStateWithLifecycle()
    val batches by viewModel.batches.collectAsStateWithLifecycle()

    var selectedBatch by remember { mutableStateOf("") }
    var selectedSheep by remember { mutableStateOf("முழு பேட்ச்") }
    var date by remember { mutableStateOf(viewModel.getTodayString()) }
    var buyer by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var kgPrice by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(batches) {
        if (selectedBatch.isBlank() && batches.isNotEmpty()) {
            selectedBatch = batches.first()
        }
    }

    val sheepOptions = remember(sheepList) {
        listOf("முழு பேட்ச்") + sheepList.map { it.no }
    }

    val autoTotal = remember(weight, kgPrice) {
        val w = weight.toDoubleOrNull() ?: 0.0
        val p = kgPrice.toDoubleOrNull() ?: 0.0
        w * p
    }

    Scaffold(
        topBar = {
            AppHeader(title = "💰 விற்பனை", onBack = onBack)
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
                            text = "💰 புதிய விற்பனை பதிவு",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Green800
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            CustomDropdownField(
                                label = "பேட்ச் நம்பர்",
                                selectedValue = selectedBatch,
                                options = batches,
                                onValueSelected = { selectedBatch = it },
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = date,
                                onValueChange = { date = it },
                                label = { Text("தேதி") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Green800,
                                    unfocusedBorderColor = Border
                                )
                            )
                        }

                        CustomDropdownField(
                            label = "ஆடு எண்",
                            selectedValue = selectedSheep,
                            options = sheepOptions,
                            onValueSelected = { selectedSheep = it },
                            placeholder = "முழு பேட்ச்"
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = buyer,
                                onValueChange = { buyer = it },
                                label = { Text("வாங்குபவர் பெயர்") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Green800,
                                    unfocusedBorderColor = Border
                                )
                            )
                            OutlinedTextField(
                                value = phone,
                                onValueChange = { phone = it },
                                label = { Text("தொலைபேசி எண்") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
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
                                value = weight,
                                onValueChange = { weight = it },
                                label = { Text("சராசரி எடை (kg)") },
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
                                value = kgPrice,
                                onValueChange = { kgPrice = it },
                                label = { Text("1 kg விலை (₹)") },
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
                            value = if (autoTotal > 0) formatMoney(autoTotal) else "-",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("மொத்த தொகை (₹) — Auto") },
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
                                if (selectedBatch.isBlank() || weight.isBlank() || kgPrice.isBlank()) {
                                    errorMessage = "தயவுசெய்து பேட்ச், எடை மற்றும் விலையை உள்ளிடவும்."
                                    return@Button
                                }
                                val w = weight.toDoubleOrNull() ?: 0.0
                                val p = kgPrice.toDoubleOrNull() ?: 0.0

                                viewModel.addSales(
                                    SalesEntity(
                                        batch = selectedBatch,
                                        sheep = if (selectedSheep == "முழு பேட்ச்") "" else selectedSheep,
                                        date = date,
                                        buyer = buyer.trim(),
                                        weight = w,
                                        phone = phone.trim(),
                                        kgPrice = p,
                                        total = autoTotal
                                    )
                                ) {
                                    buyer = ""
                                    weight = ""
                                    phone = ""
                                    kgPrice = ""
                                    errorMessage = ""
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Green800)
                        ) {
                            Text("விற்பனை செய்யப்பட்டது", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Sales Records List
            item {
                Text(
                    text = "விற்பனைப் பட்டியல் (${salesList.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Slate800,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            if (salesList.isEmpty()) {
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
                items(salesList, key = { it.id }) { item ->
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
                                        text = "பேட்ச்: ${item.batch}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = Green800
                                    )
                                    Text(
                                        text = "(${item.sheep.ifBlank { "முழு பேட்ச்" }})",
                                        fontSize = 12.sp,
                                        color = Slate500
                                    )
                                }
                                Text(
                                    text = "வாங்குபவர்: ${item.buyer.ifBlank { "-" }} | தேதி: ${item.date}",
                                    fontSize = 13.sp,
                                    color = Slate600
                                )
                                Text(
                                    text = "எடை: ${item.weight} kg @ ${formatMoney(item.kgPrice)}/kg",
                                    fontSize = 12.sp,
                                    color = Slate500
                                )
                                Text(
                                    text = "மொத்த விற்பனை: ${formatMoney(item.total)}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Green700
                                )
                            }

                            IconButton(onClick = { viewModel.deleteSales(item.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Sale",
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
