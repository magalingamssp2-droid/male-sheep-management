package com.example.malesheep.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "sheep")
data class SheepEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @SerializedName("batch") val batch: String = "",
    @SerializedName("no") val no: String = "",
    @SerializedName("market") val market: String = "",
    @SerializedName("sex") val sex: String = "ஆண்",
    @SerializedName("breed") val breed: String = "கிடா",
    @SerializedName("weight") val weight: Double = 0.0,
    @SerializedName("date") val date: String = "",
    @SerializedName("price") val price: Double = 0.0,
    @SerializedName("notes") val notes: String = "",
    @SerializedName("photo") val photo: String = ""
)

@Entity(tableName = "feed")
data class FeedEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @SerializedName("date") val date: String = "",
    @SerializedName("batch") val batch: String = "",
    @SerializedName("con") val con: Double = 0.0,
    @SerializedName("conPrice") val conPrice: Double = 0.0,
    @SerializedName("dry") val dry: Double = 0.0,
    @SerializedName("dryPrice") val dryPrice: Double = 0.0,
    @SerializedName("time") val time: String = "காலை"
)

@Entity(tableName = "sick")
data class SickEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @SerializedName("sheep") val sheep: String = "",
    @SerializedName("batch") val batch: String = "",
    @SerializedName("date") val date: String = "",
    @SerializedName("disease") val disease: String = "",
    @SerializedName("medicine") val medicine: String = "",
    @SerializedName("dose") val dose: String = "",
    @SerializedName("body") val body: String = "",
    @SerializedName("person") val person: String = "",
    @SerializedName("time") val time: String = "",
    @SerializedName("con") val con: Double = 0.0,
    @SerializedName("dry") val dry: Double = 0.0,
    @SerializedName("status") val status: String = "சிகிச்சையில்"
)

@Entity(tableName = "weight")
data class WeightEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @SerializedName("sheep") val sheep: String = "",
    @SerializedName("batch") val batch: String = "",
    @SerializedName("weight") val weight: Double = 0.0,
    @SerializedName("date") val date: String = "",
    @SerializedName("notes") val notes: String = ""
)

@Entity(tableName = "vacc")
data class VaccEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @SerializedName("batch") val batch: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("date") val date: String = "",
    @SerializedName("medicine") val medicine: String = "",
    @SerializedName("dose") val dose: String = "",
    @SerializedName("drugPrice") val drugPrice: Double = 0.0,
    @SerializedName("doctor") val doctor: Double = 0.0
)

@Entity(tableName = "stock")
data class StockEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("date") val date: String = "",
    @SerializedName("price") val price: Double = 0.0,
    @SerializedName("transport") val transport: Double = 0.0,
    @SerializedName("load") val load: Double = 0.0,
    @SerializedName("qty") val qty: Double = 0.0,
    @SerializedName("avg") val avg: Double = 0.0
)

@Entity(tableName = "sales")
data class SalesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @SerializedName("batch") val batch: String = "",
    @SerializedName("sheep") val sheep: String = "",
    @SerializedName("date") val date: String = "",
    @SerializedName("buyer") val buyer: String = "",
    @SerializedName("weight") val weight: Double = 0.0,
    @SerializedName("phone") val phone: String = "",
    @SerializedName("kgPrice") val kgPrice: Double = 0.0,
    @SerializedName("total") val total: Double = 0.0
)

@Entity(tableName = "common_expenses")
data class CommonExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @SerializedName("batch") val batch: String = "",
    @SerializedName("type") val type: String = "வண்டி வாடகை",
    @SerializedName("amount") val amount: Double = 0.0,
    @SerializedName("date") val date: String = "",
    @SerializedName("notes") val notes: String = ""
)

@Entity(tableName = "app_config")
data class AppConfigEntity(
    @PrimaryKey
    val key: String,
    val value: String
)

data class SyncDatabaseDto(
    @SerializedName("sheep") val sheep: List<SheepEntity> = emptyList(),
    @SerializedName("feed") val feed: List<FeedEntity> = emptyList(),
    @SerializedName("sick") val sick: List<SickEntity> = emptyList(),
    @SerializedName("weight") val weight: List<WeightEntity> = emptyList(),
    @SerializedName("vacc") val vacc: List<VaccEntity> = emptyList(),
    @SerializedName("stock") val stock: List<StockEntity> = emptyList(),
    @SerializedName("sales") val sales: List<SalesEntity> = emptyList(),
    @SerializedName("common") val common: List<CommonExpenseEntity> = emptyList()
)

data class GoogleApiResponse(
    val ok: Boolean = false,
    val db: SyncDatabaseDto? = null,
    val error: String? = null
)
