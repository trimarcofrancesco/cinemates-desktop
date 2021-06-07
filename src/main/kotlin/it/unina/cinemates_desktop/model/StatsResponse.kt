package it.unina.cinemates_desktop.model

import com.google.gson.annotations.SerializedName

data class StatsResponse(
    @SerializedName("usersCount") val usersCount: Int?,
    @SerializedName("reviewsCount") val reviewsCount: Int?,
    @SerializedName("searchesCount") val searchesCount: Int?
)