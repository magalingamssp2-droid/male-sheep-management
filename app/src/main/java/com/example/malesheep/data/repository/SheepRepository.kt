package com.example.malesheep.data.repository

import com.example.malesheep.data.db.AppDatabase
import com.example.malesheep.data.model.*
import com.example.malesheep.data.sync.GoogleSyncManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SheepRepository(
    private val db: AppDatabase,
    private val syncManager: GoogleSyncManager = GoogleSyncManager()
) {
    val sheep: Flow<List<SheepEntity>> = db.sheepDao().getAllSheep()
    val feed: Flow<List<FeedEntity>> = db.feedDao().getAllFeed()
    val sick: Flow<List<SickEntity>> = db.sickDao().getAllSick()
    val weight: Flow<List<WeightEntity>> = db.weightDao().getAllWeight()
    val vacc: Flow<List<VaccEntity>> = db.vaccDao().getAllVacc()
    val stock: Flow<List<StockEntity>> = db.stockDao().getAllStock()
    val sales: Flow<List<SalesEntity>> = db.salesDao().getAllSales()
    val common: Flow<List<CommonExpenseEntity>> = db.commonExpenseDao().getAllCommonExpenses()
    val apiUrl: Flow<String?> = db.appConfigDao().getConfigValueFlow("google_api_url")

    suspend fun insertSheep(entity: SheepEntity): Long = db.sheepDao().insertSheep(entity)
    suspend fun deleteSheep(id: Long) = db.sheepDao().deleteSheep(id)

    suspend fun insertFeed(entity: FeedEntity): Long = db.feedDao().insertFeed(entity)
    suspend fun deleteFeed(id: Long) = db.feedDao().deleteFeed(id)

    suspend fun insertSick(entity: SickEntity): Long = db.sickDao().insertSick(entity)
    suspend fun deleteSick(id: Long) = db.sickDao().deleteSick(id)

    suspend fun insertWeight(entity: WeightEntity): Long = db.weightDao().insertWeight(entity)
    suspend fun deleteWeight(id: Long) = db.weightDao().deleteWeight(id)

    suspend fun insertVacc(entity: VaccEntity): Long = db.vaccDao().insertVacc(entity)
    suspend fun deleteVacc(id: Long) = db.vaccDao().deleteVacc(id)

    suspend fun insertStock(entity: StockEntity): Long = db.stockDao().insertStock(entity)
    suspend fun deleteStock(id: Long) = db.stockDao().deleteStock(id)

    suspend fun insertSales(entity: SalesEntity): Long = db.salesDao().insertSales(entity)
    suspend fun deleteSales(id: Long) = db.salesDao().deleteSales(id)

    suspend fun insertCommon(entity: CommonExpenseEntity): Long = db.commonExpenseDao().insertCommonExpense(entity)
    suspend fun deleteCommon(id: Long) = db.commonExpenseDao().deleteCommonExpense(id)

    suspend fun saveApiUrl(url: String) {
        db.appConfigDao().setConfig(AppConfigEntity("google_api_url", url))
    }

    suspend fun getFullDatabaseDto(): SyncDatabaseDto = withContext(Dispatchers.IO) {
        SyncDatabaseDto(
            sheep = db.sheepDao().getAllSheepList(),
            feed = db.feedDao().getAllFeedList(),
            sick = db.sickDao().getAllSickList(),
            weight = db.weightDao().getAllWeightList(),
            vacc = db.vaccDao().getAllVaccList(),
            stock = db.stockDao().getAllStockList(),
            sales = db.salesDao().getAllSalesList(),
            common = db.commonExpenseDao().getAllCommonExpensesList()
        )
    }

    suspend fun pullFromGoogle(url: String): Result<Unit> = withContext(Dispatchers.IO) {
        val result = syncManager.pullData(url)
        result.map { dto ->
            if (dto.sheep.isNotEmpty()) {
                db.sheepDao().clearSheep()
                db.sheepDao().insertSheepList(dto.sheep)
            }
            if (dto.feed.isNotEmpty()) {
                db.feedDao().clearFeed()
                db.feedDao().insertFeedList(dto.feed)
            }
            if (dto.sick.isNotEmpty()) {
                db.sickDao().clearSick()
                db.sickDao().insertSickList(dto.sick)
            }
            if (dto.weight.isNotEmpty()) {
                db.weightDao().clearWeight()
                db.weightDao().insertWeightList(dto.weight)
            }
            if (dto.vacc.isNotEmpty()) {
                db.vaccDao().clearVacc()
                db.vaccDao().insertVaccList(dto.vacc)
            }
            if (dto.stock.isNotEmpty()) {
                db.stockDao().clearStock()
                db.stockDao().insertStockList(dto.stock)
            }
            if (dto.sales.isNotEmpty()) {
                db.salesDao().clearSales()
                db.salesDao().insertSalesList(dto.sales)
            }
            if (dto.common.isNotEmpty()) {
                db.commonExpenseDao().clearCommonExpenses()
                db.commonExpenseDao().insertCommonExpenseList(dto.common)
            }
        }
    }

    suspend fun pushToGoogle(url: String): Result<String> = withContext(Dispatchers.IO) {
        val dto = getFullDatabaseDto()
        syncManager.pushData(url, dto)
    }
}
