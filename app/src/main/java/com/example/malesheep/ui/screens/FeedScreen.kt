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
import com.example.malesheep.data.model.FeedEntity
import com.example.malesheep.ui.components.AppHeader
import com.example.malesheep.ui.components.CustomDropdownField
import com.example.malesheep.ui.components.formatMoney
import com.example.malesheep.ui.theme.*
import com.example.malesheep.ui.viewmodel.SheepViewModel

@Composable
fun FeedScreen(
    viewModel: SheepViewModel,
    onBack: () -> Unit
) {
    val feedList by viewModel.feed.collectAsStateWithLifecycle()
    val sheepList by viewModel.sheep.collectAsStateWithLifecycle()
    val batches by viewModel.batches.collectAsStateWithLifecycle()

    var selectedBatch by remember { mutableStateOf("") }
    var conKg by remember { mutableStateOf("") }
    var conPrice by remember { mutableStateOf("") }
    var dryKg by remember { mutableStateOf("") }
    var dryPrice by remember { mutableStateOf("") }
    var timeOfDay by remember { mutableStateOf("காலை") }
    var date by remember { mutableStateOf(viewModel.getTodayString()) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(batches) {
        if (selectedBatch.isBlank() && batches.isNotEmpty()) {
            selectedBatch = batches.first()
        }
    }

    val sheepCount = remember(selectedBatch, sheepList) {
        if (selectedBatch.isNotBlank()) {
            sheepList.count { it.batch == selectedBatch }
        } else 0
    }

    Scaffold(
        topBar = {
            AppHeader(title = "🌾 தீவனப் பதிவு", onBack = onBack)
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
                            text = "🌾 புதிய தீவன விவரம்",
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
                                value = if (sheepCount > 0) "$sheepCount ஆடுகள்" else "-",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("ஆடு எண்ணிக்கை") },
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
                                value = conKg,
                                onValueChange = { conKg = it },
                                label = { Text("அடர் தீவனம் (kg)") },
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
                                value = conPrice,
                                onValueChange = { conPrice = it },
                                label = { Text("அடர் விலை (₹/kg)") },
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
                                value = dryKg,
                                onValueChange = { dryKg = it },
                                label = { Text("உலர் தீவனம் (kg)") },
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
                                value = dryPrice,
                                onValueChange = { dryPrice = it },
                                label = { Text("உலர் விலை (₹/kg)") },
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
                            CustomDropdownField(
                                label = "நேரம்",
                                selectedValue = timeOfDay,
                                options = listOf("காலை", "மதியம்", "மாலை"),
                                onValueSelected = { timeOfDay = it },
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

                        if (errorMessage.isNotBlank()) {
                            Text(errorMessage, color = Red700, fontSize = 13.sp)
                        }

                        Button(
                            onClick = {
                                if (selectedBatch.isBlank()) {
                                    errorMessage = "தயவுசெய்து பேட்ச் நம்பரை தேர்வு செய்யவும்."
                                    return@Button
                                }
                                val cK = conKg.toDoubleOrNull() ?: 0.0
                                val cP = conPrice.toDoubleOrNull() ?: 0.0
                                val dK = dryKg.toDoubleOrNull() ?: 0.0
                                val dP = dryPrice.toDoubleOrNull() ?: 0.0

                                viewModel.addFeed(
                                    FeedEntity(
                                        date = date,
                                        batch = selectedBatch,
                                        con = cK,
                                        conPrice = cP,
                                        dry = dK,
                                        dryPrice = dP,
                                        time = timeOfDay
                                    )
                                ) {
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
                            Text("தீவனப் பதிவு சேமிக்க", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Feed History List
            item {
                Text(
                    text = "தீவனப் பதிவுகள் (${feedList.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Slate800,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            if (feedList.isEmpty()) {
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
                items(feedList, key = { it.id }) { item ->
                    val totalCost = (item.con * item.conPrice) + (item.dry * item.dryPrice)
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
                                        text = "(${item.date} - ${item.time})",
                                        fontSize = 12.sp,
                                        color = Slate500
                                    )
                                }
                                Text(
                                    text = "அடர்: ${item.con} kg | உலர்: ${item.dry} kg",
                                    fontSize = 13.sp,
                                    color = Slate700
                                )
                                Text(
                                    text = "மொத்த செலவு: ${formatMoney(totalCost)}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Green700
                                )
                            }

                            IconButton(onClick = { viewModel.deleteFeed(item.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Feed",
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
