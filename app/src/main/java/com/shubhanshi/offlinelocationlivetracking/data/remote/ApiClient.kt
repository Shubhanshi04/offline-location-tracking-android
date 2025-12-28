package com.shubhanshi.offlinelocationlivetracking.data.remote

import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

object ApiClient {
    private val client = OkHttpClient()
    private const val JSON = "application/json; charset = utf-8"

    fun post(url: String, json: String): Boolean {
        return try {
            val body = json.toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build()

            val response = OkHttpClient().newCall(request).execute()
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("ApiClient", "POST failed", e)
            false
        }
    }

}