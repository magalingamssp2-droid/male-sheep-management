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
import com.example.malesheep.data.model.WeightEntity
import com.example.malesheep.ui.components.AppHeader
import com.example.malesheep.ui.components.CustomDropdownField
import com.example.malesheep.ui.theme.*
import com.example.malesheep.ui.viewmodel.SheepViewModel

@Composable
fun WeightScreen(
    viewModel: SheepViewModel,
    onBack: () -> Unit
) {
    val weightList by viewModel.weight.collectAsStateWithLifecycle()
    val sheepList by viewModel.sheep.collectAsStateWithLifecycle()

    var selectedSheepNo by remember { mutableStateOf("") }
    var batch by remember { mutableStateOf("") }
    var weightVal by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(viewModel.getTodayString()) }
    var notes by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val sheepNumbers = remember(sheepList) { sheepList.map { it.no } }

    Scaffold(
        topBar = {
            AppHeader(title = "⚖️ எடை பதிவு", onBack = onBack)
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
                            text = "⚖️ புதிய எடை பதிவு",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Green800
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            CustomDropdownField(
                                label = "ஆடு எண்",
                                selectedValue = selectedSheepNo,
                                options = sheepNumbers,
                                onValueSelected = { no ->
                                    selectedSheepNo = no
                                    val matched = sheepList.find { it.no == no }
                                    batch = matched?.batch ?: ""
                                },
                                modifier = Modifier.weight(1f)
                            )

                            OutlinedTextField(
                                value = batch,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("பேட்ச்") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Green800,
                                    unfocusedBorderColor = Border
                                )
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = weightVal,
                                onValueChange = { weightVal = it },
                                label = { Text("எடை (kg)") },
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
                                if (selectedSheepNo.isBlank() || weightVal.isBlank()) {
                                    errorMessage = "தயவுசெய்து ஆடு எண் மற்றும் எடையை உள்ளிடவும்."
                                    return@Button
                                }
                                val w = weightVal.toDoubleOrNull() ?: 0.0
                                viewModel.addWeight(
                                    WeightEntity(
                                        sheep = selectedSheepNo,
                                        batch = batch,
                                        weight = w,
                                        date = date,
                                        notes = notes.trim()
                                    )
                                ) {
                                    weightVal = ""
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
                            Text("எடை சேமிக்க", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Weight Records List
            item {
                Text(
                    text = "எடை பதிவுகள் (${weightList.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Slate800,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            if (weightList.isEmpty()) {
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
                items(weightList, key = { it.id }) { item ->
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
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "ஆடு: ${item.sheep}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = Green800
                                    )
                                    Text(
                                        text = "(பேட்ச்: ${item.batch})",
                                        fontSize = 12.sp,
                                        color = Slate500
                                    )
                                    Text(
                                        text = item.date,
                                        fontSize = 12.sp,
                                        color = Slate500
                                    )
                                }
                                Text(
                                    text = "எடை: ${item.weight} kg",
                                    fontSize = 14.sp,
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

                            IconButton(onClick = { viewModel.deleteWeight(item.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Weight",
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
