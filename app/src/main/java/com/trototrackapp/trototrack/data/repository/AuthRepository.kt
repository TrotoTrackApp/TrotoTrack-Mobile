package com.trototrackapp.trototrack.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.trototrackapp.trototrack.data.ResultState
import com.trototrackapp.trototrack.data.remote.request.LoginRequest
import com.trototrackapp.trototrack.data.remote.response.LoginResponse
import com.trototrackapp.trototrack.data.remote.retrofit.ApiService

class AuthRepository(private val apiService: ApiService) {

    fun login(email: String, password: String): LiveData<ResultState<LoginResponse>> =
        liveData {
            emit(ResultState.Loading)
            try {
                val request = LoginRequest(email, password)
                val response = apiService.login(request)
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
        private var instance: AuthRepository? = null
        fun getInstance(
            apiService: ApiService,
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService)
            }.also { instance = it }
    }
}
