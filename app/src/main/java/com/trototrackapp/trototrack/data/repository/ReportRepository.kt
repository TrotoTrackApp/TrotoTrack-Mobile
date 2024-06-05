package com.trototrackapp.trototrack.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.trototrackapp.trototrack.data.ResultState
import com.trototrackapp.trototrack.data.remote.response.AddReportResponse
import com.trototrackapp.trototrack.data.remote.retrofit.ApiService
import okhttp3.MultipartBody

class ReportRepository(private val apiService: ApiService) {

    fun addReport(locationName: String, referenceLocation: String, latitude: Double, longitude: Double, image: MultipartBody.Part, statusDamage: String, description: String): LiveData<ResultState<AddReportResponse>> =
        liveData {
            emit(ResultState.Loading)
            try {
                val response = apiService.addReport(locationName, referenceLocation, latitude, longitude, image, statusDamage, description)
                if (response.isSuccessful) {
                    emit(ResultState.Success(response.body()!!))
                } else {
                    emit(ResultState.Error(response.errorBody()?.string() ?: "An error occurred"))
                }
            } catch (e: Exception) {
                emit(ResultState.Error(e.message ?: "An error occurred"))
            }
        }

    companion object {
        @Volatile
        private var instance: ReportRepository? = null
        fun getInstance(
            apiService: ApiService,
        ): ReportRepository =
            instance ?: synchronized(this) {
                instance ?: ReportRepository(apiService)
            }.also { instance = it }
    }
}