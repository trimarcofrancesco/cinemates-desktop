package it.unina.cinemates_desktop.api

import it.unina.cinemates_desktop.model.ApiResponse
import it.unina.cinemates_desktop.model.LoginResponse
import it.unina.cinemates_desktop.model.UserCredential
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    @Headers("Content-Type: application/json")
    suspend fun login(@Body credentials: UserCredential): ApiResponse<LoginResponse>
}