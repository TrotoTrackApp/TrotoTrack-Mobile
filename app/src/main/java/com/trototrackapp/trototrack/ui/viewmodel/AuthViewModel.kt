package com.trototrackapp.trototrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.trototrackapp.trototrack.data.repository.AuthRepository

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    fun login(email: String, password: String) = authRepository.login(email, password)
}