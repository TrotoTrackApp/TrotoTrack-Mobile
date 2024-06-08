package com.trototrackapp.trototrack.di

import android.content.Context
import android.util.Log
import com.trototrackapp.trototrack.data.local.UserModel
import com.trototrackapp.trototrack.data.local.UserPreference
import com.trototrackapp.trototrack.data.local.dataStore
import com.trototrackapp.trototrack.data.remote.retrofit.ApiConfig
import com.trototrackapp.trototrack.data.repository.ArticleRepository
import com.trototrackapp.trototrack.data.repository.AuthRepository
import com.trototrackapp.trototrack.data.repository.ProfileRepository
import com.trototrackapp.trototrack.data.repository.ReportRepository
import com.trototrackapp.trototrack.data.repository.ScanRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    suspend fun provideAuthRepository(): AuthRepository {
        val apiService = ApiConfig.getApiService(token = "")
        return AuthRepository.getInstance(apiService)
    }

    suspend fun provideReportRepository(context: Context): ReportRepository {
        val user = getUserFromPreference(context)
        val apiService = ApiConfig.getApiService(user.token)
        return ReportRepository.getInstance(apiService)
    }

    suspend fun provideScanRepository(context: Context): ScanRepository {
        val user = getUserFromPreference(context)
        val apiService = ApiConfig.getApiService(user.token)
        return ScanRepository.getInstance(apiService)
    }

    suspend fun provideProfileRepository(context: Context): ProfileRepository {
        val user = getUserFromPreference(context)
        val apiService = ApiConfig.getApiService(user.token)
        return ProfileRepository.getInstance(apiService)
    }

    suspend fun provideArticleRepository(context: Context): ArticleRepository {
        val user = getUserFromPreference(context)
        val apiService = ApiConfig.getApiService(user.token)
        return ArticleRepository.getInstance(apiService)
    }

    private suspend fun getUserFromPreference(context: Context): UserModel {
        val pref = UserPreference.getInstance(context.dataStore)
        return pref.getSession().first()
    }
}