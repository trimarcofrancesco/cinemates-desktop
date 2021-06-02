package it.unina.cinemates_desktop.model

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("userId") var userId: String,
    @SerializedName("email") var email: String,
    @SerializedName("username") var username: String,
    @SerializedName("profilePic") var profilePic: String = "propic1",
    @SerializedName("role") var role: String,
)