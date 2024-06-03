package com.trototrackapp.trototrack.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreference(private val context: Context) {

    private val dataStore = context.dataStore

    suspend fun saveData(id: String, name: String, username: String, email: String, token: String) {
        dataStore.edit { preferences ->
            preferences[ID_KEY] = id
            preferences[NAME_KEY] = name
            preferences[USERNAME_KEY] = username
            preferences[EMAIL_KEY] = email
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    val tokenFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        val ID_KEY = stringPreferencesKey("id")
        val NAME_KEY = stringPreferencesKey("name")
        val USERNAME_KEY = stringPreferencesKey("username")
        val EMAIL_KEY = stringPreferencesKey("email")
        val TOKEN_KEY = stringPreferencesKey("token")

        fun getInstance(context: Context): UserPreference {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserPreference(context)
            }.also { INSTANCE = it }
        }
    }
}