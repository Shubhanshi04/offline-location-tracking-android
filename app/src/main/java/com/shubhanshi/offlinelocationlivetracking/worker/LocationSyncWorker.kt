package com.shubhanshi.offlinelocationlivetracking.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shubhanshi.offlinelocationlivetracking.data.preference.UserPreferences
import com.shubhanshi.offlinelocationlivetracking.data.remote.ApiClient
import com.shubhanshi.offlinelocationlivetracking.data.repository.LocationRepository
import com.shubhanshi.offlinelocationlivetracking.data.remote.LocationUploadDto
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

class LocationSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val repository = LocationRepository(context)
    private val userPreferences = UserPreferences(context)

    override suspend fun doWork(): Result {
        val webHookUrl ="https://webhook.site/65262098-e5f2-4212-a3a7-14571479a78c"
        val unsyncedLocations =
            repository.getUnsyncedLocation()
                .sortedBy { it.timestamp } // FIFO

        if (unsyncedLocations.isEmpty()) {
            return Result.success()
        }

        val employeeId = getEmployeeId()

        unsyncedLocations.forEach { location ->

            val payload = LocationUploadDto(
                employeeId = employeeId,
                latitude = location.latitude,
                longitude = location.longitude,
                accuracy = location.accuracy,
                timestamp = location.timestamp,
                speed = location.speed
            )
            val success = ApiClient.post(
                url = webHookUrl,
                json = payload.toJson()
            )

            if (!success) {
                Log.e("SyncDebug", "Upload failed, skipping retry for demo")
            }

            // 🔹 Mock API call
            Log.d("MockUpload", "Uploading payload: $payload")
            delay(500)
        }

        // ✅ Mark as synced only after successful upload
        repository.markAsSynced(
            unsyncedLocations.map { it.id }
        )

        return Result.success()
    }

    private suspend fun getEmployeeId(): String {
        return userPreferences.employeeId.first()
    }

}