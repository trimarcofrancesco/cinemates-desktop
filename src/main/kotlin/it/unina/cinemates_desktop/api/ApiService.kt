package it.unina.cinemates_desktop.api

import it.unina.cinemates_desktop.model.*
import retrofit2.http.*

interface ApiService {
    @POST("login")
    @Headers("Content-Type: application/json")
    suspend fun login(@Body credentials: UserCredential): ApiResponse<LoginResponse>

    @GET("users/{userId}/reviews/reports/inappropriate")
    @Headers("Content-Type: application/json")
    suspend fun getInappropriates(@Path("userId") userId: String, @Header("Authorization") authorization: String): ApiResponse<InappropriatesResponse>

    @DELETE("users/{userId}/reviews/reports")
    @Headers("Content-Type: application/json")
    suspend fun removeReports(@Path("userId") userId: String, @Header("Authorization") authorization: String, @Query("deleteFrom") deleteFrom: String, @Query("itemId") itemId: Int): ApiResponse<Int>

    @DELETE("users/{userId}/reviews")
    @Headers("Content-Type: application/json")
    suspend fun removeInappropriateItem(@Path("userId") userId: String, @Header("Authorization") authorization: String, @Query("deleteFrom") deleteFrom: String, @Query("itemId") itemId: Int): ApiResponse<Int>

}