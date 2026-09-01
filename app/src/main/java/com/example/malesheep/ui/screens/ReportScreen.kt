package com.example.malesheep.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.malesheep.ui.components.AppHeader
import com.example.malesheep.ui.components.CustomDropdownField
import com.example.malesheep.ui.components.formatMoney
import com.example.malesheep.ui.theme.*
import com.example.malesheep.ui.viewmodel.SheepViewModel

@Composable
fun ReportScreen(
    viewModel: SheepViewModel,
    onBack: () -> Unit
) {
    val batches by viewModel.batches.collectAsStateWithLifecycle()
    val selectedBatch by viewModel.selectedReportBatch.collectAsStateWithLifecycle()
    val report by viewModel.batchReport.collectAsStateWithLifecycle()

    LaunchedEffect(batches) {
        if (selectedBatch.isBlank() && batches.isNotEmpty()) {
            viewModel.selectReportBatch(batches.first())
        }
    }

    Scaffold(
        topBar = {
            AppHeader(title = "📊 பேட்ச் அறிக்கை", onBack = onBack)
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
            // Batch Selection Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "பேட்ச் தேர்வு",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Green800
                        )

                        CustomDropdownField(
                            label = "பேட்ச் நம்பர்",
                            selectedValue = selectedBatch,
                            options = batches,
                            onValueSelected = { viewModel.selectReportBatch(it) }
                        )
                    }
                }
            }

            if (report != null) {
                val rep = report!!

                // Row 1: Purchase Cost & Sales Income
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ReportMetricCard(
                            label = "வாங்கிய விலை",
                            value = formatMoney(rep.purchaseCost),
                            modifier = Modifier.weight(1f)
                        )
                        ReportMetricCard(
                            label = "விற்பனை விலை",
                            value = formatMoney(rep.salesIncome),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Row 2: Con & Dry Feed Consumption
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ReportMetricCard(
                            label = "அடர் தீவனம் Consumption",
                            value = String.format(java.util.Locale.US, "%.2f kg", rep.conConsumption),
                            modifier = Modifier.weight(1f)
                        )
                        ReportMetricCard(
                            label = "உலர் தீவனம் Consumption",
                            value = String.format(java.util.Locale.US, "%.2f kg", rep.dryConsumption),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Row 3: Avg Weight & Profit/Loss
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ReportMetricCard(
                            label = "சராசரி எடை",
                            value = String.format(java.util.Locale.US, "%.2f kg", rep.avgWeight),
                            modifier = Modifier.weight(1f)
                        )
                        val isProfitable = rep.netProfit >= 0
                        ReportMetricCard(
                            label = "லாபம் / நஷ்டம்",
                            value = formatMoney(rep.netProfit),
                            valueColor = if (isProfitable) Green800 else Red700,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Row 4: Common Cost & Dead Count
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ReportMetricCard(
                            label = "பொதுச் செலவு",
                            value = formatMoney(rep.commonCost),
                            modifier = Modifier.weight(1f)
                        )
                        ReportMetricCard(
                            label = "இறந்தவை",
                            value = "${rep.deadCount}",
                            valueColor = if (rep.deadCount > 0) Red700 else Slate800,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Row 5: Total Sick Records
                item {
                    ReportMetricCard(
                        label = "நோய் பதிவுகள்",
                        value = "${rep.sickCount}",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Disease Breakdown Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "நோய்களின் காரணங்கள் / சரி செய்த விதம்",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Slate800
                            )

                            if (rep.sickRecords.isEmpty()) {
                                Text(
                                    text = "பதிவு இல்லை",
                                    fontSize = 13.sp,
                                    color = Slate500
                                )
                            } else {
                                rep.sickRecords.forEach { sick ->
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = CardDefaults.cardColors(containerColor = Slate100)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(10.dp),
                                            verticalArrangement = Arrangement.spacedBy(2.dp)
                                        ) {
                                            Text(
                                                text = "ஆடு: ${sick.sheep} — ${sick.disease} (${sick.status})",
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 13.sp,
                                                color = Slate800
                                            )
                                            Text(
                                                text = "மருந்து: ${sick.medicine.ifBlank { "மருந்து பதிவு இல்லை" }} | அளவு: ${sick.dose.ifBlank { "-" }}",
                                                fontSize = 12.sp,
                                                color = Slate600
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Text(
                            text = "அறிக்கை பார்க்க பேட்ச் தேர்வு செய்யவும்",
                            fontSize = 14.sp,
                            color = Slate500,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReportMetricCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = Green800
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                fontSize = 13.sp,
                color = Slate600,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
        }
    }
}
