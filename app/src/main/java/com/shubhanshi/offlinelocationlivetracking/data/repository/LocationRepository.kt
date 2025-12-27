package com.shubhanshi.offlinelocationlivetracking.data.repository

import android.content.Context
import com.shubhanshi.offlinelocationlivetracking.data.local.AppDatabase
import com.shubhanshi.offlinelocationlivetracking.data.local.LocationEntity

class LocationRepository(context: Context) {
    private val dao = AppDatabase
        .getInstance(context)
        .locationDao()

    suspend fun savedLocation(entity: LocationEntity){
        dao.insert(entity)
    }

    fun getLocations() = dao.getAllLocations()

    suspend fun getUnsyncedLocation() : List<LocationEntity> {
        return dao.getUnsyncedLocations()
    }

    suspend fun markAsSynced(ids : List<Long>) {
        dao.markLocationsAsSynced(ids)
    }
}