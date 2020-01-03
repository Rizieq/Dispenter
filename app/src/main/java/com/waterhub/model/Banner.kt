package com.waterhub.model


import com.google.gson.annotations.SerializedName

data class Banner(

	@field:SerializedName("Banner")
	val banner: String? = null,

	@field:SerializedName("Id")
	val id: String? = null,

	@field:SerializedName("Gender")
	val gender: String? = null,

	@field:SerializedName("Name")
	val name: String? = null


)