package it.unina.cinemates_desktop.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("profile") var profile: ProfileResponse,
    @SerializedName("AccessToken") var accessToken: String,
    @SerializedName("ExpiresIn") var expiresIn: Long? = null,
    @SerializedName("RefreshToken") var refreshToken: String,
    @SerializedName("IdToken") var idToken: String
)

data class UserTokens(
    var accessToken: String,
    var expiresIn: Long? = null,
    var refreshToken: String,
    var idToken: String
)

fun LoginResponse.toUser(): User {
    return User(this)
}