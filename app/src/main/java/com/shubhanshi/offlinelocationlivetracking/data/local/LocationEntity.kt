package com.shubhanshi.offlinelocationlivetracking.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    val latitude : Double,
    val longitude : Double,
    val accuracy : Float,
    val timestamp: Long,
    val speed : Float,
    val isSynced: Boolean = false // to know which location still needs to be synced

)
