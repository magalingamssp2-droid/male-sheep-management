package com.example.malesheep.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.malesheep.data.model.*

@Database(
    entities = [
        SheepEntity::class,
        FeedEntity::class,
        SickEntity::class,
        WeightEntity::class,
        VaccEntity::class,
        StockEntity::class,
        SalesEntity::class,
        CommonExpenseEntity::class,
        AppConfigEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sheepDao(): SheepDao
    abstract fun feedDao(): FeedDao
    abstract fun sickDao(): SickDao
    abstract fun weightDao(): WeightDao
    abstract fun vaccDao(): VaccDao
    abstract fun stockDao(): StockDao
    abstract fun salesDao(): SalesDao
    abstract fun commonExpenseDao(): CommonExpenseDao
    abstract fun appConfigDao(): AppConfigDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "male_sheep_management.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
