package com.trototrackapp.trototrack.di

import android.content.Context
import com.trototrackapp.trototrack.data.remote.retrofit.ApiConfig
import com.trototrackapp.trototrack.data.repository.AuthRepository

object Injection {
    fun provideRepository(context: Context): AuthRepository {
        val apiService = ApiConfig.getApiService()
        return AuthRepository.getInstance(apiService)
    }
}