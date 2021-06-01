package it.unina.cinemates_desktop.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("profile") var profile: Profile,
    @SerializedName("AccessToken") var accessToken: String,
    @SerializedName("ExpiresIn") var expiresIn: Long? = null,
    @SerializedName("RefreshToken") var refreshToken: String,
    @SerializedName("IdToken") var idToken: String
)