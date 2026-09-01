package com.example.malesheep.data.sync

import com.example.malesheep.data.model.GoogleApiResponse
import com.example.malesheep.data.model.SyncDatabaseDto
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class GoogleSyncManager {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .followRedirects(true)
        .followSslRedirects(true)
        .build()

    private val gson = Gson()

    suspend fun pullData(apiUrl: String): Result<SyncDatabaseDto> = withContext(Dispatchers.IO) {
        try {
            val url = if (apiUrl.contains("?")) {
                "$apiUrl&action=get&t=${System.currentTimeMillis()}"
            } else {
                "$apiUrl?action=get&t=${System.currentTimeMillis()}"
            }

            val request = Request.Builder()
                .url(url)
                .get()
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("HTTP Error: ${response.code}"))
            }

            var body = response.body?.string() ?: ""
            // Handle JSONP callback wrappers if present like googleCb_1234({...})
            if (body.startsWith("googleCb_") || body.contains("(") && body.endsWith(");")) {
                val start = body.indexOf("(")
                val end = body.lastIndexOf(")")
                if (start != -1 && end != -1 && end > start) {
                    body = body.substring(start + 1, end).trim()
                }
            } else if (body.startsWith("(") && body.endsWith(")")) {
                body = body.substring(1, body.length - 1).trim()
            }

            val apiResponse = try {
                gson.fromJson(body, GoogleApiResponse::class.java)
            } catch (e: Exception) {
                // Try parsing directly as SyncDatabaseDto
                val dbDto = gson.fromJson(body, SyncDatabaseDto::class.java)
                GoogleApiResponse(ok = true, db = dbDto)
            }

            if (apiResponse != null && (apiResponse.ok || apiResponse.db != null)) {
                Result.success(apiResponse.db ?: SyncDatabaseDto())
            } else {
                Result.failure(Exception(apiResponse?.error ?: "Invalid response from server"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun pushData(apiUrl: String, dbDto: SyncDatabaseDto): Result<String> = withContext(Dispatchers.IO) {
        try {
            val payload = gson.toJson(dbDto)
            val formBody = FormBody.Builder()
                .add("action", "merge")
                .add("payload", payload)
                .build()

            val request = Request.Builder()
                .url(apiUrl)
                .post(formBody)
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                Result.success("Sync successful")
            } else {
                Result.failure(Exception("Sync failed with code: ${response.code}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
