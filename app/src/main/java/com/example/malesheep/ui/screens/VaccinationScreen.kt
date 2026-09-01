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
import com.example.malesheep.data.model.VaccEntity
import com.example.malesheep.ui.components.AppHeader
import com.example.malesheep.ui.components.CustomDropdownField
import com.example.malesheep.ui.components.formatMoney
import com.example.malesheep.ui.theme.*
import com.example.malesheep.ui.viewmodel.SheepViewModel

@Composable
fun VaccinationScreen(
    viewModel: SheepViewModel,
    onBack: () -> Unit
) {
    val vaccList by viewModel.vacc.collectAsStateWithLifecycle()
    val batches by viewModel.batches.collectAsStateWithLifecycle()

    var selectedBatch by remember { mutableStateOf("") }
    var vaccName by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(viewModel.getTodayString()) }
    var medicine by remember { mutableStateOf("") }
    var dose by remember { mutableStateOf("") }
    var drugPrice by remember { mutableStateOf("") }
    var doctorFee by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(batches) {
        if (selectedBatch.isBlank() && batches.isNotEmpty()) {
            selectedBatch = batches.first()
        }
    }

    Scaffold(
        topBar = {
            AppHeader(title = "💉 தடுப்பூசி பதிவு", onBack = onBack)
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
                            text = "💉 புதிய தடுப்பூசி பதிவு",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Green800
                        )
                        Text(
                            text = "ஒரே தடுப்பூசி பதிவை ஒரு பேட்சிற்கு பயன்படுத்தலாம்.",
                            fontSize = 12.sp,
                            color = Slate500
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
                                value = vaccName,
                                onValueChange = { vaccName = it },
                                label = { Text("தடுப்பூசி பெயர்") },
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
                            OutlinedTextField(
                                value = medicine,
                                onValueChange = { medicine = it },
                                label = { Text("மருந்து பெயர்") },
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
                            value = dose,
                            onValueChange = { dose = it },
                            label = { Text("அளவு") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Green800,
                                unfocusedBorderColor = Border
                            )
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = drugPrice,
                                onValueChange = { drugPrice = it },
                                label = { Text("மருந்து விலை (₹)") },
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
                                value = doctorFee,
                                onValueChange = { doctorFee = it },
                                label = { Text("டாக்டர் செலவு (₹)") },
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

                        if (errorMessage.isNotBlank()) {
                            Text(errorMessage, color = Red700, fontSize = 13.sp)
                        }

                        Button(
                            onClick = {
                                if (selectedBatch.isBlank() || vaccName.isBlank()) {
                                    errorMessage = "தயவுசெய்து பேட்ச் மற்றும் தடுப்பூசி பெயரை உள்ளிடவும்."
                                    return@Button
                                }
                                val dP = drugPrice.toDoubleOrNull() ?: 0.0
                                val doc = doctorFee.toDoubleOrNull() ?: 0.0

                                viewModel.addVacc(
                                    VaccEntity(
                                        batch = selectedBatch,
                                        name = vaccName.trim(),
                                        date = date,
                                        medicine = medicine.trim(),
                                        dose = dose.trim(),
                                        drugPrice = dP,
                                        doctor = doc
                                    )
                                ) {
                                    vaccName = ""
                                    medicine = ""
                                    dose = ""
                                    drugPrice = ""
                                    doctorFee = ""
                                    errorMessage = ""
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Green800)
                        ) {
                            Text("தடுப்பூசி சேமிக்க", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Vaccination List
            item {
                Text(
                    text = "தடுப்பூசி பதிவுகள் (${vaccList.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Slate800,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            if (vaccList.isEmpty()) {
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
                items(vaccList, key = { it.id }) { item ->
                    val totalCost = item.drugPrice + item.doctor
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
                                        text = "(பேட்ச்: ${item.batch})",
                                        fontSize = 12.sp,
                                        color = Slate500
                                    )
                                }
                                Text(
                                    text = "தேதி: ${item.date} | மருந்து: ${item.medicine.ifBlank { "-" }}",
                                    fontSize = 13.sp,
                                    color = Slate600
                                )
                                Text(
                                    text = "மருந்து: ${formatMoney(item.drugPrice)} | டாக்டர்: ${formatMoney(item.doctor)}",
                                    fontSize = 12.sp,
                                    color = Slate500
                                )
                                Text(
                                    text = "மொத்த செலவு: ${formatMoney(totalCost)}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Green700
                                )
                            }

                            IconButton(onClick = { viewModel.deleteVacc(item.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Vaccine",
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
