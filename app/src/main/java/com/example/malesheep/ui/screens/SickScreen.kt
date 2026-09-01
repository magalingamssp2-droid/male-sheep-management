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
import com.example.malesheep.data.model.SickEntity
import com.example.malesheep.ui.components.AppHeader
import com.example.malesheep.ui.components.CustomDropdownField
import com.example.malesheep.ui.components.StatusBadge
import com.example.malesheep.ui.theme.*
import com.example.malesheep.ui.viewmodel.SheepViewModel

@Composable
fun SickScreen(
    viewModel: SheepViewModel,
    onBack: () -> Unit
) {
    val sickList by viewModel.sick.collectAsStateWithLifecycle()
    val sheepList by viewModel.sheep.collectAsStateWithLifecycle()

    var selectedSheepNo by remember { mutableStateOf("") }
    var batch by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(viewModel.getTodayString()) }
    var disease by remember { mutableStateOf("") }
    var medicine by remember { mutableStateOf("") }
    var dose by remember { mutableStateOf("") }
    var bodyPart by remember { mutableStateOf("") }
    var personName by remember { mutableStateOf("") }
    var injectionTime by remember { mutableStateOf("") }
    var conKg by remember { mutableStateOf("") }
    var dryKg by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("சிகிச்சையில்") }
    var errorMessage by remember { mutableStateOf("") }

    val sheepNumbers = remember(sheepList) { sheepList.map { it.no } }

    Scaffold(
        topBar = {
            AppHeader(title = "🩺 நோய்வாய்ப்பட்டவை", onBack = onBack)
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
                            text = "🩺 புதிய நோய் / சிகிச்சை பதிவு",
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
                                label = { Text("பேட்ச் நம்பர்") },
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
                                value = disease,
                                onValueChange = { disease = it },
                                label = { Text("நோய் வகை") },
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
                            OutlinedTextField(
                                value = dose,
                                onValueChange = { dose = it },
                                label = { Text("அளவு") },
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
                                value = bodyPart,
                                onValueChange = { bodyPart = it },
                                label = { Text("ஊசி போட்ட உடல் பகுதி") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Green800,
                                    unfocusedBorderColor = Border
                                )
                            )
                            OutlinedTextField(
                                value = personName,
                                onValueChange = { personName = it },
                                label = { Text("போட்டவர் பெயர்") },
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
                                value = injectionTime,
                                onValueChange = { injectionTime = it },
                                label = { Text("நேரம்") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Green800,
                                    unfocusedBorderColor = Border
                                )
                            )
                            CustomDropdownField(
                                label = "இன்றைய நிலை",
                                selectedValue = status,
                                options = listOf("சிகிச்சையில்", "குணமடைந்தது", "தொடர்கிறது", "இறந்தது"),
                                onValueSelected = { status = it },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = conKg,
                                onValueChange = { conKg = it },
                                label = { Text("அடர் தீவனம் kg") },
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
                                value = dryKg,
                                onValueChange = { dryKg = it },
                                label = { Text("உலர் தீவனம் kg") },
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
                                if (selectedSheepNo.isBlank() || disease.isBlank()) {
                                    errorMessage = "தயவுசெய்து ஆடு எண் மற்றும் நோய் வகையை உள்ளிடவும்."
                                    return@Button
                                }
                                val cK = conKg.toDoubleOrNull() ?: 0.0
                                val dK = dryKg.toDoubleOrNull() ?: 0.0

                                viewModel.addSick(
                                    SickEntity(
                                        sheep = selectedSheepNo,
                                        batch = batch,
                                        date = date,
                                        disease = disease.trim(),
                                        medicine = medicine.trim(),
                                        dose = dose.trim(),
                                        body = bodyPart.trim(),
                                        person = personName.trim(),
                                        time = injectionTime.trim(),
                                        con = cK,
                                        dry = dK,
                                        status = status
                                    )
                                ) {
                                    disease = ""
                                    medicine = ""
                                    dose = ""
                                    bodyPart = ""
                                    conKg = ""
                                    dryKg = ""
                                    errorMessage = ""
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Green800)
                        ) {
                            Text("நோய் பதிவு சேமிக்க", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Sick List
            item {
                Text(
                    text = "சிகிச்சை / நோய் பதிவுகள் (${sickList.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Slate800,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            if (sickList.isEmpty()) {
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
                items(sickList, key = { it.id }) { item ->
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
                                        text = "(${item.date})",
                                        fontSize = 12.sp,
                                        color = Slate500
                                    )
                                    StatusBadge(status = item.status)
                                }
                                Text(
                                    text = "நோய்: ${item.disease} | மருந்து: ${item.medicine.ifBlank { "-" }}",
                                    fontSize = 13.sp,
                                    color = Slate700
                                )
                                if (item.dose.isNotBlank() || item.body.isNotBlank()) {
                                    Text(
                                        text = "அளவு: ${item.dose} | உடல் பகுதி: ${item.body}",
                                        fontSize = 12.sp,
                                        color = Slate600
                                    )
                                }
                            }

                            IconButton(onClick = { viewModel.deleteSick(item.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Sick Record",
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
