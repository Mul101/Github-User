package com.codemul.githubuser.data.network

import com.codemul.githubuser.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        private const val BASE_URL = "https://api.github.com"
        private const val TOKEN = BuildConfig.GITHUB_TOKEN

        private fun getApiService(): OkHttpClient {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
            client.addInterceptor { chain ->
                val origin = chain.request()
                val request = origin.newBuilder()
                    .addHeader("Authorization", TOKEN)
                    .build()
                chain.proceed(request)
            }
                .addInterceptor(loggingInterceptor)
                .build()

            return client.build()
        }

        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getApiService())
            .build()

        val apiService: ApiService = retrofit.create(ApiService::class.java)
    }
}