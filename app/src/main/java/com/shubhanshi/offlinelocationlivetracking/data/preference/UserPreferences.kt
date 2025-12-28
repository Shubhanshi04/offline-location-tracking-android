package com.shubhanshi.offlinelocationlivetracking.data.preference

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        private val EMP_ID = stringPreferencesKey("employee_id")
    }

    val employeeId: Flow<String> =
        context.dataStore.data.map { prefs ->
            prefs[EMP_ID] ?: ""
        }

    suspend fun saveEmployeeId(id: String) {
        context.dataStore.edit { prefs ->
            prefs[EMP_ID] = id
        }

    }
}