package com.trototrackapp.trototrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.trototrackapp.trototrack.data.repository.ReportRepository

class DetailReportViewModel(private val reportRepository: ReportRepository) : ViewModel() {

    fun getReportDetail(reportId: String) =
        reportRepository.getReportDetail(reportId)

    fun voteReport(id: String) =
        reportRepository.voteReport(id)
}