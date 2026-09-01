package com.example.malesheep.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.malesheep.data.model.SheepEntity
import com.example.malesheep.ui.components.AppHeader
import com.example.malesheep.ui.components.CustomDropdownField
import com.example.malesheep.ui.components.formatMoney
import com.example.malesheep.ui.theme.*
import com.example.malesheep.ui.viewmodel.SheepViewModel
import java.io.ByteArrayOutputStream
import java.io.InputStream

@Composable
fun SheepScreen(
    viewModel: SheepViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val sheepList by viewModel.sheep.collectAsStateWithLifecycle()

    var batch by remember { mutableStateOf("") }
    var sheepNo by remember { mutableStateOf("") }
    var market by remember { mutableStateOf("சந்தை 1") }
    var sex by remember { mutableStateOf("ஆண்") }
    var breed by remember { mutableStateOf("கிடா") }
    var weight by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(viewModel.getTodayString()) }
    var price by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var photoBase64 by remember { mutableStateOf("") }
    var photoBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var errorMessage by remember { mutableStateOf("") }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val originalBitmap = BitmapFactory.decodeStream(inputStream)
                if (originalBitmap != null) {
                    val scaled = Bitmap.createScaledBitmap(
                        originalBitmap,
                        (originalBitmap.width * 0.5f).toInt().coerceAtLeast(100),
                        (originalBitmap.height * 0.5f).toInt().coerceAtLeast(100),
                        true
                    )
                    photoBitmap = scaled
                    val outputStream = ByteArrayOutputStream()
                    scaled.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
                    val byteArray = outputStream.toByteArray()
                    photoBase64 = "data:image/jpeg;base64," + Base64.encodeToString(byteArray, Base64.NO_WRAP)
                }
            } catch (e: Exception) {
                errorMessage = "புகைப்படம் சேர்க்க முடியவில்லை: ${e.message}"
            }
        }
    }

    Scaffold(
        topBar = {
            AppHeader(title = "🐑 புதிய ஆடு சேர்க்கை", onBack = onBack)
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
                            text = "🐑 புதிய ஆடு விவரங்கள்",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Green800
                        )

                        // Photo Picker Area
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Slate100)
                                    .border(1.dp, Slate500, RoundedCornerShape(12.dp))
                                    .clickable { photoPickerLauncher.launch("image/*") },
                                contentAlignment = Alignment.Center
                            ) {
                                if (photoBitmap != null) {
                                    Image(
                                        bitmap = photoBitmap!!.asImageBitmap(),
                                        contentDescription = "Sheep Photo",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AddPhotoAlternate,
                                            contentDescription = "Upload Photo",
                                            tint = Slate500
                                        )
                                        Text("புகைப்படம்", fontSize = 11.sp, color = Slate500)
                                    }
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text("ஆட்டின் புகைப்படம்", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                Text("கேலரியில் இருந்து படம் தேர்வு செய்யவும்", fontSize = 12.sp, color = Slate500)
                            }
                        }

                        // Form Inputs
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = batch,
                                onValueChange = { batch = it },
                                label = { Text("பேட்ச் நம்பர்") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Green800,
                                    unfocusedBorderColor = Border
                                )
                            )
                            OutlinedTextField(
                                value = sheepNo,
                                onValueChange = { sheepNo = it },
                                label = { Text("ஆடு எண்") },
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
                                label = "வாங்கிய சந்தை",
                                selectedValue = market,
                                options = listOf("சந்தை 1", "சந்தை 2", "மற்றது"),
                                onValueSelected = { market = it },
                                modifier = Modifier.weight(1f)
                            )
                            CustomDropdownField(
                                label = "பாலினம்",
                                selectedValue = sex,
                                options = listOf("ஆண்", "பெண்"),
                                onValueSelected = { sex = it },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        CustomDropdownField(
                            label = "இனம்",
                            selectedValue = breed,
                            options = listOf("கிடா", "மேச்சேரி", "கொடி ஆடு", "செம்மறியாடு", "கலப்பு இனம்"),
                            onValueSelected = { breed = it }
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = weight,
                                onValueChange = { weight = it },
                                label = { Text("தற்போதைய எடை (kg)") },
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

                        OutlinedTextField(
                            value = price,
                            onValueChange = { price = it },
                            label = { Text("வாங்கிய விலை (₹)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Green800,
                                unfocusedBorderColor = Border
                            )
                        )

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
                                if (batch.isBlank() || sheepNo.isBlank() || weight.isBlank()) {
                                    errorMessage = "தயவுசெய்து பேட்ச், ஆடு எண் மற்றும் எடையை உள்ளிடவும்."
                                    return@Button
                                }
                                val wVal = weight.toDoubleOrNull() ?: 0.0
                                val pVal = price.toDoubleOrNull() ?: 0.0
                                viewModel.addSheep(
                                    SheepEntity(
                                        batch = batch.trim(),
                                        no = sheepNo.trim(),
                                        market = market,
                                        sex = sex,
                                        breed = breed,
                                        weight = wVal,
                                        date = date,
                                        price = pVal,
                                        notes = notes.trim(),
                                        photo = photoBase64
                                    )
                                ) {
                                    sheepNo = ""
                                    weight = ""
                                    price = ""
                                    notes = ""
                                    photoBase64 = ""
                                    photoBitmap = null
                                    errorMessage = ""
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Green800)
                        ) {
                            Text("ஆடு சேமிக்க", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Sheep List Card
            item {
                Text(
                    text = "ஆடுகள் பட்டியல் (${sheepList.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Slate800,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            if (sheepList.isEmpty()) {
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
                items(sheepList, key = { it.id }) { item ->
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
                                        text = "ஆடு: ${item.no}",
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
                                    text = "இனம்: ${item.breed} | பாலினம்: ${item.sex}",
                                    fontSize = 13.sp,
                                    color = Slate600
                                )
                                Text(
                                    text = "எடை: ${item.weight} kg | விலை: ${formatMoney(item.price)}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Slate700
                                )
                                if (item.notes.isNotBlank()) {
                                    Text(
                                        text = "குறிப்பு: ${item.notes}",
                                        fontSize = 12.sp,
                                        color = Slate500
                                    )
                                }
                            }

                            IconButton(onClick = { viewModel.deleteSheep(item.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Sheep",
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
