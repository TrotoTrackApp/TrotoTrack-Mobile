package com.trototrackapp.trototrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.trototrackapp.trototrack.data.repository.ReportRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class GetReportsViewModel(private val reportRepository: ReportRepository) : ViewModel() {

    fun getAllReports() =
        reportRepository.getAllReports()
}