package com.trototrackapp.trototrack.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.trototrackapp.trototrack.data.ResultState
import com.trototrackapp.trototrack.data.remote.response.AddReportResponse
import com.trototrackapp.trototrack.data.remote.response.DetailReportResponse
import com.trototrackapp.trototrack.data.remote.response.GetAllReportsResponse
import com.trototrackapp.trototrack.data.remote.response.GetReportsUserResponse
import com.trototrackapp.trototrack.data.remote.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ReportRepository(private val apiService: ApiService) {

    fun addReport(locationName: RequestBody, referenceLocation: RequestBody, latitude: RequestBody, longitude: RequestBody, image: MultipartBody.Part, status_damage: RequestBody, description: RequestBody): LiveData<ResultState<AddReportResponse>> =
        liveData {
            emit(ResultState.Loading)
            try {
                val response = apiService.addReport(locationName, referenceLocation, latitude, longitude, image, status_damage, description)
                if (response.isSuccessful) {
                    emit(ResultState.Success(response.body()!!))
                } else {
                    emit(ResultState.Error(response.errorBody()?.string() ?: "An error occurred"))
                }
            } catch (e: Exception) {
                emit(ResultState.Error(e.message ?: "An error occurred"))
            }
        }

    fun getAllReports(): LiveData<ResultState<GetAllReportsResponse>> =
        liveData {
            emit(ResultState.Loading)
            try {
                val response = apiService.getAllReports()
                if (response.isSuccessful) {
                    emit(ResultState.Success(response.body()!!))
                } else {
                    emit(ResultState.Error(response.errorBody()?.string() ?: "An error occurred"))
                }
            } catch (e: Exception) {
                emit(ResultState.Error(e.message ?: "An error occurred"))
            }
        }

    fun getReportDetail(reportId: String): LiveData<ResultState<DetailReportResponse>> =
        liveData {
            emit(ResultState.Loading)
            try {
                val response = apiService.getReportDetailById(reportId)
                if (response.isSuccessful) {
                    emit(ResultState.Success(response.body()!!))
                } else {
                    emit(ResultState.Error(response.errorBody()?.string() ?: "An error occurred"))
                }
            } catch (e: Exception) {
                emit(ResultState.Error(e.message ?: "An error occurred"))
            }
        }

    fun getReportsUser(): LiveData<ResultState<GetReportsUserResponse>> =
        liveData {
            emit(ResultState.Loading)
            try {
                val response = apiService.getReportUser()
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