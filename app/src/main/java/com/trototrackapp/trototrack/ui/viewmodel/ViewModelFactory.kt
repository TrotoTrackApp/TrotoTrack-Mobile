// ViewModelFactory.kt
package com.trototrackapp.trototrack.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trototrackapp.trototrack.data.repository.AuthRepository
import com.trototrackapp.trototrack.data.repository.ProfileRepository
import com.trototrackapp.trototrack.data.repository.ReportRepository
import com.trototrackapp.trototrack.data.repository.ScanRepository
import com.trototrackapp.trototrack.di.Injection
import kotlinx.coroutines.runBlocking

class ViewModelFactory private constructor(
    private val authRepository: AuthRepository,
    private val reportRepository: ReportRepository,
    private val scanRepository: ScanRepository,
    private val profileRepository: ProfileRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(authRepository) as T
            }
            modelClass.isAssignableFrom(AddReportViewModel::class.java) -> {
                AddReportViewModel(reportRepository) as T
            }
            modelClass.isAssignableFrom(ScanViewModel::class.java) -> {
                ScanViewModel(scanRepository) as T
            }
            modelClass.isAssignableFrom(GetReportsViewModel::class.java) -> {
                GetReportsViewModel(reportRepository) as T
            }
            modelClass.isAssignableFrom(DetailReportViewModel::class.java) -> {
                DetailReportViewModel(reportRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(profileRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: runBlocking {
                    ViewModelFactory(
                        Injection.provideAuthRepository(),
                        Injection.provideReportRepository(context),
                        Injection.provideScanRepository(context),
                        Injection.provideProfileRepository(context)
                    ).also { instance = it }
                }
            }
    }
}
