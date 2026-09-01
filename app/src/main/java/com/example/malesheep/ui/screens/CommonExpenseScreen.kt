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
import com.example.malesheep.data.model.CommonExpenseEntity
import com.example.malesheep.ui.components.AppHeader
import com.example.malesheep.ui.components.CustomDropdownField
import com.example.malesheep.ui.components.formatMoney
import com.example.malesheep.ui.theme.*
import com.example.malesheep.ui.viewmodel.SheepViewModel

@Composable
fun CommonExpenseScreen(
    viewModel: SheepViewModel,
    onBack: () -> Unit
) {
    val commonList by viewModel.common.collectAsStateWithLifecycle()
    val batches by viewModel.batches.collectAsStateWithLifecycle()

    var selectedBatch by remember { mutableStateOf("") }
    var expenseType by remember { mutableStateOf("வண்டி வாடகை") }
    var amount by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(viewModel.getTodayString()) }
    var notes by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(batches) {
        if (selectedBatch.isBlank() && batches.isNotEmpty()) {
            selectedBatch = batches.first()
        }
    }

    Scaffold(
        topBar = {
            AppHeader(title = "🧾 பேட்ச் பொதுச் செலவு", onBack = onBack)
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
                            text = "🧾 புதிய பொதுச் செலவு பதிவு",
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
                            CustomDropdownField(
                                label = "செலவு வகை",
                                selectedValue = expenseType,
                                options = listOf("வண்டி வாடகை", "மற்ற பொதுச் செலவு"),
                                onValueSelected = { expenseType = it },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = amount,
                                onValueChange = { amount = it },
                                label = { Text("செலவு தொகை (₹)") },
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

                        OutlinedTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            label = { Text("குறிப்புகள்") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
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
                                if (selectedBatch.isBlank() || amount.isBlank()) {
                                    errorMessage = "தயவுசெய்து பேட்ச் மற்றும் தொகையை உள்ளிடவும்."
                                    return@Button
                                }
                                val a = amount.toDoubleOrNull() ?: 0.0

                                viewModel.addCommonExpense(
                                    CommonExpenseEntity(
                                        batch = selectedBatch,
                                        type = expenseType,
                                        amount = a,
                                        date = date,
                                        notes = notes.trim()
                                    )
                                ) {
                                    amount = ""
                                    notes = ""
                                    errorMessage = ""
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Green800)
                        ) {
                            Text("பொதுச் செலவு சேமிக்க", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Common Expenses Records List
            item {
                Text(
                    text = "பொதுச் செலவுகள் பட்டியல் (${commonList.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Slate800,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            if (commonList.isEmpty()) {
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
                items(commonList, key = { it.id }) { item ->
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
                                        text = item.type,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = Green800
                                    )
                                    Text(
                                        text = "(பேட்ச்: ${item.batch})",
                                        fontSize = 12.sp,
                                        color = Slate500
                                    )
                                }
                                Text(
                                    text = "தேதி: ${item.date} | தொகை: ${formatMoney(item.amount)}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Green700
                                )
                                if (item.notes.isNotBlank()) {
                                    Text(
                                        text = "குறிப்பு: ${item.notes}",
                                        fontSize = 12.sp,
                                        color = Slate600
                                    )
                                }
                            }

                            IconButton(onClick = { viewModel.deleteCommonExpense(item.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Expense",
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
