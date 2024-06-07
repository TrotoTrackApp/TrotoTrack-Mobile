package com.trototrackapp.trototrack.di

import android.content.Context
import android.util.Log
import com.trototrackapp.trototrack.data.local.UserPreference
import com.trototrackapp.trototrack.data.remote.retrofit.ApiConfig
import com.trototrackapp.trototrack.data.repository.AuthRepository
import com.trototrackapp.trototrack.data.repository.ReportRepository
import com.trototrackapp.trototrack.data.repository.ScanRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val apiService = ApiConfig.getApiService(token = "")
        return AuthRepository.getInstance(apiService)
    }

    fun provideReportRepository(context: Context): ReportRepository {
        val pref = UserPreference.getInstance(context)
        val token = runBlocking { pref.getToken() }
        Log.d("Injection", "Token: $token")
        val apiService = ApiConfig.getApiService(token)
        return ReportRepository.getInstance(apiService)
    }

    fun provideScanRepository(context: Context): ScanRepository {
        val pref = UserPreference.getInstance(context)
        val token = runBlocking { pref.getToken() }
        val apiService = ApiConfig.getApiService(token)
        return ScanRepository.getInstance(apiService)
    }
}