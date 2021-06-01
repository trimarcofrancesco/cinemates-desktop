package it.unina.cinemates_desktop.di

import it.unina.cinemates_desktop.api.ApiService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://dcdlyx3jbj.execute-api.us-east-2.amazonaws.com/v1/")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .serializeNulls()
                    .serializeSpecialFloatingPointValues()
                    .setLenient()
                    .create()
            ))
        .client(provideOkHttpClient(provideLoggingInterceptor()))
        .build()


    private fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor()

        logger.level = HttpLoggingInterceptor.Level.BODY

        return logger
    }

    fun buildService(): ApiService {
        return retrofit.create(ApiService::class.java)
    }

}
