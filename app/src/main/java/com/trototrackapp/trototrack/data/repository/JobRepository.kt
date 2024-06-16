package com.trototrackapp.trototrack.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.trototrackapp.trototrack.data.ResultState
import com.trototrackapp.trototrack.data.remote.response.GetArticleResponse
import com.trototrackapp.trototrack.data.remote.response.GetJobResponse
import com.trototrackapp.trototrack.data.remote.response.JobResponse
import com.trototrackapp.trototrack.data.remote.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class JobRepository(private val apiService: ApiService) {

    fun job(name: RequestBody, nik: RequestBody, address: RequestBody, phone: RequestBody, file: MultipartBody.Part): LiveData<ResultState<JobResponse>> =
        liveData {
            emit(ResultState.Loading)
            try {
                val response = apiService.job(name, nik, address, phone, file)
                if (response.isSuccessful) {
                    emit(ResultState.Success(response.body()!!))
                } else {
                    emit(ResultState.Error(response.errorBody()?.string() ?: "An error occurred"))
                }
            } catch (e: Exception) {
                emit(ResultState.Error(e.message ?: "An error occurred"))
            }
        }

    fun getJob(): LiveData<ResultState<GetJobResponse>> =
        liveData {
            emit(ResultState.Loading)
            try {
                val response = apiService.getJob()
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
        private var instance: JobRepository? = null
        fun getInstance(
            apiService: ApiService,
        ): JobRepository =
            instance ?: synchronized(this) {
                instance ?: JobRepository(apiService)
            }.also { instance = it }
    }
}