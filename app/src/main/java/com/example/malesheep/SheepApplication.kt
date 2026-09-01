package com.example.malesheep

import android.app.Application
import com.example.malesheep.data.db.AppDatabase
import com.example.malesheep.data.repository.SheepRepository
import com.example.malesheep.data.sync.GoogleSyncManager

class SheepApplication : Application() {
    val database by lazy { AppDatabase.getInstance(this) }
    val repository by lazy { SheepRepository(database, GoogleSyncManager()) }
}
