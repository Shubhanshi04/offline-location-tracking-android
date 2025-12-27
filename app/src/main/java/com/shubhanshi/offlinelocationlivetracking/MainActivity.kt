package com.shubhanshi.offlinelocationlivetracking

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.shubhanshi.offlinelocationlivetracking.data.repository.LocationRepository
import com.shubhanshi.offlinelocationlivetracking.service.LocationTrackingService
import com.shubhanshi.offlinelocationlivetracking.ui.NetworkObserver
import com.shubhanshi.offlinelocationlivetracking.ui.theme.OfflineLocationLiveTrackingTheme
import com.shubhanshi.offlinelocationlivetracking.worker.LocationSyncWorker

class MainActivity : ComponentActivity() {

    private lateinit var serviceIntent: Intent

    @SuppressLint("NewApi")
    private val permissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val fineGranted =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            val coarseGranted =
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            val notificationGranted =
                if (Build.VERSION.SDK_INT >= 33)
                    permissions[Manifest.permission.POST_NOTIFICATIONS] == true
                else true

            if (fineGranted && coarseGranted && notificationGranted) {
                startForegroundService(serviceIntent)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        serviceIntent = Intent(this, LocationTrackingService::class.java)
        enableEdgeToEdge()
        setContent {
            OfflineLocationLiveTrackingTheme {
                val repository = remember {
                    LocationRepository(applicationContext)
                }
                val pendingCount by repository
                    .getPendingLogs()
                    .collectAsState(initial = 0)

                val networkObserver = remember {
                    NetworkObserver(applicationContext)
                }
                val isOnline by networkObserver.isOnline.collectAsState()

                CenterButtons(
                    pendingCount = pendingCount,
                    isOnline = isOnline,
                    refresh = {
                        triggerLocationSync(this)
                    },
                    onStartClick = {
                        requestPermissionsAndStart()
                        triggerLocationSync(this)
                    },
                    onStopClick = { stopService(serviceIntent) }
                )
            }
        }
    }

    private fun requestPermissionsAndStart() {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (Build.VERSION.SDK_INT >= 33) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        permissionLauncher.launch(permissions.toTypedArray())
    }
}

private fun triggerLocationSync(context: Context) {
    val constraints = androidx.work.Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val syncRequest = OneTimeWorkRequestBuilder<LocationSyncWorker>()
        .setConstraints(constraints)
        .build()

    WorkManager
        .getInstance(context)
        .enqueue(syncRequest)
}

@Composable
private fun CenterButtons(
    pendingCount: Int,
    isOnline: Boolean,
    refresh: () -> Unit,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = if (pendingCount > 0) "Pending Logs : $pendingCount" else "No Pending Logs 🎉")

            IconButton(onClick = refresh) {
                Icon(Icons.Default.Refresh, contentDescription = "Refresh")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onStartClick) {
            Text("Start Tracking")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onStopClick) {
            Text("Stop Tracking")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text =
                if (isOnline) "Status : Online 🟢" else "Offline\nCheck your internet ❗️"
        )
    }
}
