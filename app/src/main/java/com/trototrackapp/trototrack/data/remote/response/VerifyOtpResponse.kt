package com.trototrackapp.trototrack.data.remote.response

import com.google.gson.annotations.SerializedName

data class VerifyOtpResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)
