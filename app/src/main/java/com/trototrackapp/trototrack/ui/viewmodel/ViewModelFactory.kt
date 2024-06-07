package com.trototrackapp.trototrack.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trototrackapp.trototrack.data.repository.AuthRepository
import com.trototrackapp.trototrack.data.repository.ReportRepository
import com.trototrackapp.trototrack.data.repository.ScanRepository
import com.trototrackapp.trototrack.di.Injection

class ViewModelFactory private constructor(
    private val authRepository: AuthRepository,
    private val reportRepository: ReportRepository,
    private val scanRepository: ScanRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(authRepository) as T
        } else if (modelClass.isAssignableFrom(AddReportViewModel::class.java)) {
            return AddReportViewModel(reportRepository) as T
        } else if (modelClass.isAssignableFrom(ScanViewModel::class.java)) {
            return ScanViewModel(scanRepository) as T
        } else if (modelClass.isAssignableFrom(GetReportsViewModel::class.java)) {
            return GetReportsViewModel(reportRepository) as T
        } else if (modelClass.isAssignableFrom(DetailReportViewModel::class.java)) {
            return DetailReportViewModel(reportRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideAuthRepository(context),
                    Injection.provideReportRepository(context),
                    Injection.provideScanRepository(context)
                ).also { instance = it }
            }
    }
}
