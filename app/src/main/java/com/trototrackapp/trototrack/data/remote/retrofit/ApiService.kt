package com.trototrackapp.trototrack.data.remote.retrofit

import com.trototrackapp.trototrack.data.remote.request.LoginRequest
import com.trototrackapp.trototrack.data.remote.request.RegisterRequest
import com.trototrackapp.trototrack.data.remote.response.LoginResponse
import com.trototrackapp.trototrack.data.remote.response.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>
}