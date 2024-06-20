package com.trototrackapp.trototrack.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetJobResponse(

	@field:SerializedName("data")
	val data: DataJob? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataJob(

	@field:SerializedName("nik")
	val nik: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("file")
	val file: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
