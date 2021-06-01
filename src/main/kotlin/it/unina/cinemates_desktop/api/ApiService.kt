package it.unina.cinemates_desktop.api

import it.unina.cinemates_desktop.model.ApiResponse
import it.unina.cinemates_desktop.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    @Headers("Content-Type: application/json")
    suspend fun login(@Body credentials: HashMap<String, String>): ApiResponse<LoginResponse>
}