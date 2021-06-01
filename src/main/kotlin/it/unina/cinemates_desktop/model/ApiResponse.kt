package it.unina.cinemates_desktop.model

import com.google.gson.annotations.SerializedName

class ApiResponse<T> {
    @SerializedName("result")
    val result: T? = null
}