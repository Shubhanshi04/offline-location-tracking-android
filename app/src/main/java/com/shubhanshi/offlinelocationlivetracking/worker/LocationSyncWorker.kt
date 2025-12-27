package com.shubhanshi.offlinelocationlivetracking.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shubhanshi.offlinelocationlivetracking.data.repository.LocationRepository
import kotlinx.coroutines.delay

class LocationSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    private val repository = LocationRepository(context)

    override suspend fun doWork(): Result {
        val unsynced = repository.getUnsyncedLocation()

        if(unsynced.isEmpty()){
            return Result.success()
        }
        // 🔹 Simulate API call
        // (Later replace with real network request)

        delay(1000)
        val ids = unsynced.map{it.id}
        repository.markAsSynced(ids)

        return Result.success()
    }
}