package com.trototrackapp.trototrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.trototrackapp.trototrack.data.repository.ReportRepository
import okhttp3.MultipartBody

class AddReportViewModel(private val reportRepository: ReportRepository) : ViewModel() {

    fun addReport(locationName: String, referenceLocation: String, latitude: Double, longitude: Double, image: MultipartBody.Part, statusDamage: String, description: String) =
        reportRepository.addReport(locationName, referenceLocation, latitude, longitude, image, statusDamage, description)
}