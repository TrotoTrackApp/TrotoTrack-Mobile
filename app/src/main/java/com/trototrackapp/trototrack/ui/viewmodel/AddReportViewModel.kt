package com.trototrackapp.trototrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.trototrackapp.trototrack.data.repository.ReportRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddReportViewModel(private val reportRepository: ReportRepository) : ViewModel() {

    fun addReport(locationName: RequestBody, reference_location: RequestBody, latitude: RequestBody, longitude: RequestBody, image: MultipartBody.Part, status_damage: RequestBody, description: RequestBody) =
        reportRepository.addReport(locationName, reference_location, latitude, longitude, image, status_damage, description)
}