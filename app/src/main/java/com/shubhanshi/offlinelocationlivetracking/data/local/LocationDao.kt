package com.shubhanshi.offlinelocationlivetracking.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: LocationEntity)

    @Query("SELECT * from locations ORDER by timestamp DESC")
    fun getAllLocations(): Flow<List<LocationEntity>>

    @Query("DELETE FROM locations")
    suspend fun clearAll()

    @Query("SELECT * from locations WHERE isSynced = 0")
    suspend fun getUnsyncedLocations() : List<LocationEntity>

    @Query("SELECT COUNT(*) from locations WHERE isSynced = 0")
    fun getPendingCounts() : Flow<Int>

    @Query("UPDATE locations SET isSynced = 1 WHERE id IN (:ids)")
    suspend fun markLocationsAsSynced(ids: List<Long>)
}