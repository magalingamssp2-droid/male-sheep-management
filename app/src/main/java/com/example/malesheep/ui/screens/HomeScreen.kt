package com.example.malesheep.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.malesheep.ui.components.AppHeader
import com.example.malesheep.ui.components.formatMoney
import com.example.malesheep.ui.theme.*
import com.example.malesheep.ui.viewmodel.SheepViewModel

@Composable
fun HomeScreen(
    viewModel: SheepViewModel,
    onNavigate: (String) -> Unit
) {
    val stats by viewModel.dashboardStats.collectAsStateWithLifecycle()
    val savedApiUrl by viewModel.apiUrl.collectAsStateWithLifecycle()
    val syncStatus by viewModel.syncStatus.collectAsStateWithLifecycle()
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()

    var inputApiUrl by remember(savedApiUrl) { mutableStateOf(savedApiUrl ?: "") }

    Scaffold(
        topBar = {
            AppHeader(title = "🐑 ஆண் ஆடு மேலாண்மை")
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
            // Google Drive / Sheets Sync Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(2.dp, Color(0xFF86EFAC)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "☁️ Google Drive / Google Sheets Sync",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Slate800
                        )
                        Text(
                            text = "இந்த முகவரி Google Apps Script Web App URL. ஒரே Google Sheet-ஐ இரண்டு மொபைல்களும் பயன்படுத்தலாம்.",
                            fontSize = 12.sp,
                            color = Slate500,
                            lineHeight = 16.sp
                        )

                        OutlinedTextField(
                            value = inputApiUrl,
                            onValueChange = { inputApiUrl = it },
                            placeholder = { Text("https://script.google.com/macros/s/...../exec", fontSize = 13.sp) },
                            label = { Text("Google Apps Script URL") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Green800,
                                unfocusedBorderColor = Border
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    viewModel.saveApiUrl(inputApiUrl)
                                    viewModel.pullFromGoogle()
                                },
                                enabled = !isSyncing,
                                colors = ButtonDefaults.buttonColors(containerColor = Green800),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("☁️ Connect & Sync", fontSize = 13.sp)
                            }

                            Button(
                                onClick = { viewModel.pullFromGoogle() },
                                enabled = !isSyncing,
                                colors = ButtonDefaults.buttonColors(containerColor = Slate500),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("↻ Refresh", fontSize = 13.sp)
                            }
                        }

                        if (isSyncing) {
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth(),
                                color = Green800
                            )
                        }

                        syncStatus?.let { (msg, isOk) ->
                            Text(
                                text = msg,
                                fontSize = 12.sp,
                                color = if (isOk) Green800 else Red700,
                                fontWeight = FontWeight.Medium
                            )
                        } ?: run {
                            Text(
                                text = if (savedApiUrl.isNullOrBlank()) "இணைப்பு அமைக்கப்படவில்லை." else "இணைப்பு தயாராக உள்ளது.",
                                fontSize = 12.sp,
                                color = Slate500
                            )
                        }
                    }
                }
            }

            // Dashboard Stats (2x2 grid)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "மொத்த ஆடுகள்",
                        value = "${stats.totalSheep}",
                        onClick = { onNavigate("sheep") },
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "நோய்வாய்ப்பட்டவை",
                        value = "${stats.totalSick}",
                        onClick = { onNavigate("sick") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "இன்றைய அடர் தீவனம்",
                        value = String.format(java.util.Locale.US, "%.2f kg", stats.todayCon),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "இன்றைய உலர் தீவனம்",
                        value = String.format(java.util.Locale.US, "%.2f kg", stats.todayDry),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Today Feed Cost Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "இன்றைய தீவனச் செலவு",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Slate700
                        )
                        Text(
                            text = formatMoney(stats.todayFeedCost),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Green800
                        )
                    }
                }
            }

            // Menu Options (முக்கிய பகுதிகள்)
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
                            text = "முக்கிய பகுதிகள்",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate800
                        )

                        val menuItems = listOf(
                            Triple("sheep", "🐑 ஆடுகள்", "Sheep"),
                            Triple("feed", "🌾 தீவனம்", "Feed Log"),
                            Triple("sick", "🩺 நோய்வாய்ப்பட்டவை", "Sick & Care"),
                            Triple("weight", "⚖️ எடை பதிவு", "Weight Log"),
                            Triple("vacc", "💉 தடுப்பூசி பதிவு", "Vaccination"),
                            Triple("stock", "🌾 தீவனம் இருப்பு", "Feed Stock"),
                            Triple("sales", "💰 விற்பனை", "Sales"),
                            Triple("common", "🧾 பேட்ச் பொதுச் செலவு", "Common Expense"),
                            Triple("report", "📊 அறிக்கை", "Batch Report")
                        )

                        // 2 column grid of menu buttons
                        for (i in menuItems.indices step 2) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val item1 = menuItems[i]
                                OutlinedButton(
                                    onClick = { onNavigate(item1.first) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(52.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = Color.White,
                                        contentColor = Green800
                                    ),
                                    border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
                                    contentPadding = PaddingValues(horizontal = 8.dp)
                                ) {
                                    Text(
                                        text = item1.second,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1
                                    )
                                }

                                if (i + 1 < menuItems.size) {
                                    val item2 = menuItems[i + 1]
                                    OutlinedButton(
                                        onClick = { onNavigate(item2.first) },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(52.dp),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = Color.White,
                                            contentColor = Green800
                                        ),
                                        border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
                                        contentPadding = PaddingValues(horizontal = 8.dp)
                                    ) {
                                        Text(
                                            text = item2.second,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            maxLines = 1
                                        )
                                    }
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                fontSize = 13.sp,
                color = Slate600,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Green800
            )
        }
    }
}
