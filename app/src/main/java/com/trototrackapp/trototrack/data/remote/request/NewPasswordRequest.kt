package com.trototrackapp.trototrack.data.remote.request

data class NewPasswordRequest (
    val email: String,
    val password: String,
    val confirm_passwrod: String
)