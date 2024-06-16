package com.trototrackapp.trototrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.trototrackapp.trototrack.data.repository.JobRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class JobViewModel(private val jobRepository: JobRepository) : ViewModel() {

    fun getJob() =
        jobRepository.getJob()

    fun job(name: RequestBody, nik: RequestBody, address: RequestBody, phone: RequestBody, file: MultipartBody.Part) =
        jobRepository.job(name, nik, address, phone, file)
}