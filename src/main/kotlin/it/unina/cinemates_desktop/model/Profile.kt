package it.unina.cinemates_desktop.model

import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("userId") var userId: String,
    @SerializedName("email") var email: String,
    @SerializedName("username") var username: String,
    @SerializedName("profilePic") var profilePic: String = "propic1",
    @SerializedName("role") var role: String,
    @SerializedName("createdAt") var createdAt: String,
    @SerializedName("favoriteQuote") var favoriteQuote: String = "",
    @SerializedName("provider") var provider: String? = null,
)