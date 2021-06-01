package it.unina.cinemates_desktop.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("error") val error: String,
    @SerializedName("message") val message: String
)