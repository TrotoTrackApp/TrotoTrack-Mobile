package com.trototrackapp.trototrack.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreference(private val context: Context) {

    private val dataStore = context.dataStore

    suspend fun saveData(id: String, token: String) {
        dataStore.edit { preferences ->
            preferences[ID_KEY] = id
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun getToken(): String {
        return dataStore.data.first()[TOKEN_KEY] ?: ""
    }

    suspend fun getId(): String {
        return dataStore.data.first()[ID_KEY] ?: ""
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        val ID_KEY = stringPreferencesKey("id")
        val TOKEN_KEY = stringPreferencesKey("token")

        fun getInstance(context: Context): UserPreference {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserPreference(context)
            }.also { INSTANCE = it }
        }
    }
}