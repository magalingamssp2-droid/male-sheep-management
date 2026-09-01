package com.example.malesheep.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.malesheep.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SheepDao {
    @Query("SELECT * FROM sheep ORDER BY id DESC")
    fun getAllSheep(): Flow<List<SheepEntity>>

    @Query("SELECT * FROM sheep ORDER BY id DESC")
    suspend fun getAllSheepList(): List<SheepEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSheep(sheep: SheepEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSheepList(sheep: List<SheepEntity>)

    @Query("DELETE FROM sheep WHERE id = :id")
    suspend fun deleteSheep(id: Long)

    @Query("DELETE FROM sheep")
    suspend fun clearSheep()
}

@Dao
interface FeedDao {
    @Query("SELECT * FROM feed ORDER BY id DESC")
    fun getAllFeed(): Flow<List<FeedEntity>>

    @Query("SELECT * FROM feed ORDER BY id DESC")
    suspend fun getAllFeedList(): List<FeedEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeed(feed: FeedEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedList(feed: List<FeedEntity>)

    @Query("DELETE FROM feed WHERE id = :id")
    suspend fun deleteFeed(id: Long)

    @Query("DELETE FROM feed")
    suspend fun clearFeed()
}

@Dao
interface SickDao {
    @Query("SELECT * FROM sick ORDER BY id DESC")
    fun getAllSick(): Flow<List<SickEntity>>

    @Query("SELECT * FROM sick ORDER BY id DESC")
    suspend fun getAllSickList(): List<SickEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSick(sick: SickEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSickList(sick: List<SickEntity>)

    @Query("DELETE FROM sick WHERE id = :id")
    suspend fun deleteSick(id: Long)

    @Query("DELETE FROM sick")
    suspend fun clearSick()
}

@Dao
interface WeightDao {
    @Query("SELECT * FROM weight ORDER BY id DESC")
    fun getAllWeight(): Flow<List<WeightEntity>>

    @Query("SELECT * FROM weight ORDER BY id DESC")
    suspend fun getAllWeightList(): List<WeightEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeight(weight: WeightEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeightList(weight: List<WeightEntity>)

    @Query("DELETE FROM weight WHERE id = :id")
    suspend fun deleteWeight(id: Long)

    @Query("DELETE FROM weight")
    suspend fun clearWeight()
}

@Dao
interface VaccDao {
    @Query("SELECT * FROM vacc ORDER BY id DESC")
    fun getAllVacc(): Flow<List<VaccEntity>>

    @Query("SELECT * FROM vacc ORDER BY id DESC")
    suspend fun getAllVaccList(): List<VaccEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacc(vacc: VaccEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVaccList(vacc: List<VaccEntity>)

    @Query("DELETE FROM vacc WHERE id = :id")
    suspend fun deleteVacc(id: Long)

    @Query("DELETE FROM vacc")
    suspend fun clearVacc()
}

@Dao
interface StockDao {
    @Query("SELECT * FROM stock ORDER BY id DESC")
    fun getAllStock(): Flow<List<StockEntity>>

    @Query("SELECT * FROM stock ORDER BY id DESC")
    suspend fun getAllStockList(): List<StockEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stock: StockEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStockList(stock: List<StockEntity>)

    @Query("DELETE FROM stock WHERE id = :id")
    suspend fun deleteStock(id: Long)

    @Query("DELETE FROM stock")
    suspend fun clearStock()
}

@Dao
interface SalesDao {
    @Query("SELECT * FROM sales ORDER BY id DESC")
    fun getAllSales(): Flow<List<SalesEntity>>

    @Query("SELECT * FROM sales ORDER BY id DESC")
    suspend fun getAllSalesList(): List<SalesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSales(sales: SalesEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSalesList(sales: List<SalesEntity>)

    @Query("DELETE FROM sales WHERE id = :id")
    suspend fun deleteSales(id: Long)

    @Query("DELETE FROM sales")
    suspend fun clearSales()
}

@Dao
interface CommonExpenseDao {
    @Query("SELECT * FROM common_expenses ORDER BY id DESC")
    fun getAllCommonExpenses(): Flow<List<CommonExpenseEntity>>

    @Query("SELECT * FROM common_expenses ORDER BY id DESC")
    suspend fun getAllCommonExpensesList(): List<CommonExpenseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommonExpense(expense: CommonExpenseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommonExpenseList(expenses: List<CommonExpenseEntity>)

    @Query("DELETE FROM common_expenses WHERE id = :id")
    suspend fun deleteCommonExpense(id: Long)

    @Query("DELETE FROM common_expenses")
    suspend fun clearCommonExpenses()
}

@Dao
interface AppConfigDao {
    @Query("SELECT value FROM app_config WHERE `key` = :key LIMIT 1")
    fun getConfigValueFlow(key: String): Flow<String?>

    @Query("SELECT value FROM app_config WHERE `key` = :key LIMIT 1")
    suspend fun getConfigValue(key: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setConfig(config: AppConfigEntity)
}
