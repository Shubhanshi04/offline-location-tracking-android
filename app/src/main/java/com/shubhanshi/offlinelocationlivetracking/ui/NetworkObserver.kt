package com.shubhanshi.offlinelocationlivetracking.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NetworkObserver(context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _isOnline = MutableStateFlow(false)
    val isOnline : StateFlow<Boolean> = _isOnline

    init {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                _isOnline.value = true
            }

            override fun onLost(network: Network) {
                _isOnline.value = false
            }
        }
        connectivityManager.registerDefaultNetworkCallback(callback)
    }


}