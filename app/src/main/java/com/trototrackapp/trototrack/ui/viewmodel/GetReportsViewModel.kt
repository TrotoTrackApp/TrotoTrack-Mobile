package com.trototrackapp.trototrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.trototrackapp.trototrack.data.repository.ReportRepository

class GetReportsViewModel(private val reportRepository: ReportRepository) : ViewModel() {

    fun getAllReports() =
        reportRepository.getAllReports()

    fun getReportsUser() =
        reportRepository.getReportsUser()
}