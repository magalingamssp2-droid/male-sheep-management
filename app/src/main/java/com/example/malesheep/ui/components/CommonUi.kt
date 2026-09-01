package com.example.malesheep.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.malesheep.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

fun formatMoney(amount: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale("en", "IN"))
    formatter.maximumFractionDigits = 2
    return "₹" + formatter.format(amount)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(
    title: String,
    onBack: (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
        },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Green800
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownField(
    label: String,
    selectedValue: String,
    options: List<String>,
    onValueSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "தேர்வு செய்யவும்"
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedValue.ifBlank { placeholder },
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Green800,
                unfocusedBorderColor = Border
            ),
            shape = RoundedCornerShape(10.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onValueSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bg, textColor) = when (status) {
        "குணமடைந்தது" -> Pair(Green100, Green900)
        "இறந்தது" -> Pair(Red50, Red700)
        "சிகிச்சையில்" -> Pair(Amber50, Amber700)
        else -> Pair(Slate100, Slate700)
    }

    Box(
        modifier = modifier
            .background(bg, shape = RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = status,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
