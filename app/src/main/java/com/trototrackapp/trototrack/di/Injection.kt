package com.trototrackapp.trototrack.di

import android.content.Context
import com.trototrackapp.trototrack.data.local.UserPreference
import com.trototrackapp.trototrack.data.remote.retrofit.ApiConfig
import com.trototrackapp.trototrack.data.repository.AuthRepository
import com.trototrackapp.trototrack.data.repository.ReportRepository
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
        val apiService = ApiConfig.getApiService(token)
        return ReportRepository.getInstance(apiService)
    }
}