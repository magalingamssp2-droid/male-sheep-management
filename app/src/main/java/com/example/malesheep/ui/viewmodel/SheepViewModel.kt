package com.example.malesheep.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.malesheep.data.model.*
import com.example.malesheep.data.repository.SheepRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class DashboardStats(
    val totalSheep: Int = 0,
    val totalSick: Int = 0,
    val todayCon: Double = 0.0,
    val todayDry: Double = 0.0,
    val todayFeedCost: Double = 0.0
)

data class BatchReport(
    val batch: String = "",
    val purchaseCost: Double = 0.0,
    val conConsumption: Double = 0.0,
    val dryConsumption: Double = 0.0,
    val avgWeight: Double = 0.0,
    val salesIncome: Double = 0.0,
    val feedCost: Double = 0.0,
    val vaccCost: Double = 0.0,
    val commonCost: Double = 0.0,
    val netProfit: Double = 0.0,
    val deadCount: Int = 0,
    val sickCount: Int = 0,
    val sickRecords: List<SickEntity> = emptyList()
)

class SheepViewModel(
    private val repository: SheepRepository
) : ViewModel() {

    val sheep = repository.sheep.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val feed = repository.feed.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val sick = repository.sick.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val weight = repository.weight.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val vacc = repository.vacc.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val stock = repository.stock.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val sales = repository.sales.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val common = repository.common.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val apiUrl = repository.apiUrl.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    private val _syncStatus = MutableStateFlow<Pair<String, Boolean>?>(null)
    val syncStatus: StateFlow<Pair<String, Boolean>?> = _syncStatus.asStateFlow()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    private val _selectedReportBatch = MutableStateFlow<String>("")
    val selectedReportBatch: StateFlow<String> = _selectedReportBatch.asStateFlow()

    fun getTodayString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    val dashboardStats: StateFlow<DashboardStats> = combine(sheep, sick, feed) { sList, sickList, fList ->
        val todayStr = getTodayString()
        val totalSheep = sList.size
        val totalSick = sickList.count { it.status != "குணமடைந்தது" }
        val todayFeeds = fList.filter { it.date == todayStr }
        val todayCon = todayFeeds.sumOf { it.con }
        val todayDry = todayFeeds.sumOf { it.dry }
        val todayFeedCost = todayFeeds.sumOf { (it.con * it.conPrice) + (it.dry * it.dryPrice) }
        DashboardStats(
            totalSheep = totalSheep,
            totalSick = totalSick,
            todayCon = todayCon,
            todayDry = todayDry,
            todayFeedCost = todayFeedCost
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardStats())

    val batches: StateFlow<List<String>> = sheep.map { list ->
        list.map { it.batch.trim() }.filter { it.isNotEmpty() }.distinct()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private data class BatchDataGroupA(
        val batch: String,
        val sheep: List<SheepEntity>,
        val feed: List<FeedEntity>,
        val vacc: List<VaccEntity>
    )

    private data class BatchDataGroupB(
        val common: List<CommonExpenseEntity>,
        val sales: List<SalesEntity>,
        val sick: List<SickEntity>
    )

    private val batchDataGroupA: Flow<BatchDataGroupA> = combine(
        selectedReportBatch, sheep, feed, vacc
    ) { batch, sList, fList, vList ->
        BatchDataGroupA(batch, sList, fList, vList)
    }

    private val batchDataGroupB: Flow<BatchDataGroupB> = combine(
        common, sales, sick
    ) { cList, saList, skList ->
        BatchDataGroupB(cList, saList, skList)
    }

    val batchReport: StateFlow<BatchReport?> = combine(
        batchDataGroupA, batchDataGroupB
    ) { groupA, groupB ->
        val batch = groupA.batch
        if (batch.isBlank()) return@combine null

        val batchSheep = groupA.sheep.filter { it.batch == batch }
        val batchFeed = groupA.feed.filter { it.batch == batch }
        val batchVacc = groupA.vacc.filter { it.batch == batch }
        val batchCommon = groupB.common.filter { it.batch == batch }
        val batchSales = groupB.sales.filter { it.batch == batch }
        val batchSick = groupB.sick.filter { it.batch == batch }

        val purchaseCost = batchSheep.sumOf { it.price }
        val conConsumption = batchFeed.sumOf { it.con }
        val dryConsumption = batchFeed.sumOf { it.dry }
        val feedCost = batchFeed.sumOf { (it.con * it.conPrice) + (it.dry * it.dryPrice) }
        val vaccCost = batchVacc.sumOf { it.drugPrice + it.doctor }
        val commonCost = batchCommon.sumOf { it.amount }
        val salesIncome = batchSales.sumOf { it.total }

        val avgWeight = if (batchSheep.isNotEmpty()) batchSheep.sumOf { it.weight } / batchSheep.size else 0.0
        val deadCount = batchSick.count { it.status == "இறந்தது" }
        val sickCount = batchSick.size
        val netProfit = salesIncome - (purchaseCost + feedCost + vaccCost + commonCost)

        BatchReport(
            batch = batch,
            purchaseCost = purchaseCost,
            conConsumption = conConsumption,
            dryConsumption = dryConsumption,
            avgWeight = avgWeight,
            salesIncome = salesIncome,
            feedCost = feedCost,
            vaccCost = vaccCost,
            commonCost = commonCost,
            netProfit = netProfit,
            deadCount = deadCount,
            sickCount = sickCount,
            sickRecords = batchSick
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun selectReportBatch(batch: String) {
        _selectedReportBatch.value = batch
    }

    fun addSheep(sheepEntity: SheepEntity, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.insertSheep(sheepEntity)
            triggerAutoSync()
            onComplete()
        }
    }

    fun deleteSheep(id: Long) {
        viewModelScope.launch {
            repository.deleteSheep(id)
            triggerAutoSync()
        }
    }

    fun addFeed(feedEntity: FeedEntity, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.insertFeed(feedEntity)
            triggerAutoSync()
            onComplete()
        }
    }

    fun deleteFeed(id: Long) {
        viewModelScope.launch {
            repository.deleteFeed(id)
            triggerAutoSync()
        }
    }

    fun addSick(sickEntity: SickEntity, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.insertSick(sickEntity)
            triggerAutoSync()
            onComplete()
        }
    }

    fun deleteSick(id: Long) {
        viewModelScope.launch {
            repository.deleteSick(id)
            triggerAutoSync()
        }
    }

    fun addWeight(weightEntity: WeightEntity, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.insertWeight(weightEntity)
            triggerAutoSync()
            onComplete()
        }
    }

    fun deleteWeight(id: Long) {
        viewModelScope.launch {
            repository.deleteWeight(id)
            triggerAutoSync()
        }
    }

    fun addVacc(vaccEntity: VaccEntity, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.insertVacc(vaccEntity)
            triggerAutoSync()
            onComplete()
        }
    }

    fun deleteVacc(id: Long) {
        viewModelScope.launch {
            repository.deleteVacc(id)
            triggerAutoSync()
        }
    }

    fun addStock(stockEntity: StockEntity, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.insertStock(stockEntity)
            triggerAutoSync()
            onComplete()
        }
    }

    fun deleteStock(id: Long) {
        viewModelScope.launch {
            repository.deleteStock(id)
            triggerAutoSync()
        }
    }

    fun addSales(salesEntity: SalesEntity, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.insertSales(salesEntity)
            triggerAutoSync()
            onComplete()
        }
    }

    fun deleteSales(id: Long) {
        viewModelScope.launch {
            repository.deleteSales(id)
            triggerAutoSync()
        }
    }

    fun addCommonExpense(commonEntity: CommonExpenseEntity, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.insertCommon(commonEntity)
            triggerAutoSync()
            onComplete()
        }
    }

    fun deleteCommonExpense(id: Long) {
        viewModelScope.launch {
            repository.deleteCommon(id)
            triggerAutoSync()
        }
    }

    fun saveApiUrl(url: String) {
        viewModelScope.launch {
            repository.saveApiUrl(url.trim())
            _syncStatus.value = Pair("Google Script URL சேமிக்கப்பட்டது", true)
        }
    }

    fun pullFromGoogle() {
        val currentUrl = apiUrl.value?.trim().orEmpty()
        if (currentUrl.isBlank()) {
            _syncStatus.value = Pair("முதலில் Google Apps Script URL கொடுக்கவும்.", false)
            return
        }

        viewModelScope.launch {
            _isSyncing.value = true
            _syncStatus.value = Pair("Google-லிருந்து data பெறுகிறது…", true)
            val result = repository.pullFromGoogle(currentUrl)
            _isSyncing.value = false
            if (result.isSuccess) {
                _syncStatus.value = Pair("✓ Google-லிருந்து data update செய்யப்பட்டது", true)
            } else {
                _syncStatus.value = Pair("Google data பெற முடியவில்லை: ${result.exceptionOrNull()?.message}", false)
            }
        }
    }

    fun pushToGoogle() {
        val currentUrl = apiUrl.value?.trim().orEmpty()
        if (currentUrl.isBlank()) {
            _syncStatus.value = Pair("Local-ஆக சேமிக்கப்பட்டது. Google URL இன்னும் அமைக்கப்படவில்லை.", false)
            return
        }

        viewModelScope.launch {
            _isSyncing.value = true
            _syncStatus.value = Pair("Google Drive-க்கு save request அனுப்புகிறது…", true)
            val result = repository.pushToGoogle(currentUrl)
            _isSyncing.value = false
            if (result.isSuccess) {
                _syncStatus.value = Pair("✓ Google Sheets-க்கு data அனுப்பப்பட்டது", true)
            } else {
                _syncStatus.value = Pair("Google save error: ${result.exceptionOrNull()?.message}", false)
            }
        }
    }

    private fun triggerAutoSync() {
        val currentUrl = apiUrl.value?.trim().orEmpty()
        if (currentUrl.isNotBlank()) {
            viewModelScope.launch {
                repository.pushToGoogle(currentUrl)
            }
        }
    }
}

class SheepViewModelFactory(private val repository: SheepRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SheepViewModel::class.java)) {
            return SheepViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
