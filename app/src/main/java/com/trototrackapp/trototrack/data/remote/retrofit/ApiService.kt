package com.trototrackapp.trototrack.data.remote.retrofit

import com.trototrackapp.trototrack.data.remote.request.LoginRequest
import com.trototrackapp.trototrack.data.remote.request.RegisterRequest
import com.trototrackapp.trototrack.data.remote.request.UpdateProfileRequest
import com.trototrackapp.trototrack.data.remote.response.AddReportResponse
import com.trototrackapp.trototrack.data.remote.response.DetailReportResponse
import com.trototrackapp.trototrack.data.remote.response.GetAllReportsResponse
import com.trototrackapp.trototrack.data.remote.response.GetProfileResponse
import com.trototrackapp.trototrack.data.remote.response.GetReportsUserResponse
import com.trototrackapp.trototrack.data.remote.response.LoginResponse
import com.trototrackapp.trototrack.data.remote.response.RegisterResponse
import com.trototrackapp.trototrack.data.remote.response.ScanResponse
import com.trototrackapp.trototrack.data.remote.response.UpdateProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

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
        @Part("location") location: RequestBody,
        @Part("reference_location") reference_location: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("status_damage") status_damage: RequestBody,
        @Part("description") description: RequestBody
    ): Response<AddReportResponse>

    @GET("reports")
    suspend fun getAllReports(): Response<GetAllReportsResponse>

    @GET("reports/{id}")
    suspend fun getReportDetailById(@Path("id") id: String): Response<DetailReportResponse>

    @GET("reports/profile")
    suspend fun getReportUser(): Response<GetReportsUserResponse>

    @PUT("profile")
    suspend fun updateProfile(
        @Body request: UpdateProfileRequest
    ): Response<UpdateProfileResponse>

    @GET("profile")
    suspend fun getUserProfile(): Response<GetProfileResponse>

    @Multipart
    @POST("scan")
    suspend fun scan(
        @Part image: MultipartBody.Part,
    ): Response<ScanResponse>
}