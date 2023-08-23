package com.example.project.data


import com.google.gson.annotations.SerializedName

data class PhoneCodesItem(
    @SerializedName("code")
    val code: String,
    @SerializedName("dial_code")
    val dialCode: String,
    @SerializedName("flag")
    val flag: String,
    @SerializedName("name")
    val name: String,
    var isSelected: Boolean = false
)