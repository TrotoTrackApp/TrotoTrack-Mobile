package com.trototrackapp.trototrack.data.remote.retrofit

import com.trototrackapp.trototrack.data.remote.request.LoginRequest
import com.trototrackapp.trototrack.data.remote.request.RegisterRequest
import com.trototrackapp.trototrack.data.remote.response.AddReportResponse
import com.trototrackapp.trototrack.data.remote.response.LoginResponse
import com.trototrackapp.trototrack.data.remote.response.RegisterResponse
import com.trototrackapp.trototrack.data.remote.response.ScanResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    @Multipart
    @POST("reports")
    suspend fun addReport(
        @Part("location") location: String,
        @Part("reference_location") reference_location: String,
        @Part("latitude") latitude: Double,
        @Part("longitude") longitude: Double,
        @Part image: MultipartBody.Part,
        @Part("status_damage") statusDamage: String,
        @Part("description") description: String
    ): Response<AddReportResponse>

    @Multipart
    @POST("reports")
    suspend fun scan(
        @Part image: MultipartBody.Part,
    ): Response<ScanResponse>
}