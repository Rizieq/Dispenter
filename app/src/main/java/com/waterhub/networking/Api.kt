package com.waterhub.networking

import com.waterhub.data.service.UsersService
import retrofit2.Retrofit
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


class Api {
    companion object {
        private const val baseUrl = "http://api.waterhub.id"

        private fun retrofitOLD(): Retrofit {

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(loggingInterceptor)

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
        }


        private fun retrofitNEW(): Retrofit {

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(loggingInterceptor)

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun usersService(): UsersService = retrofitOLD().create(UsersService::class.java)
        fun getClient(): UsersService = retrofitNEW().create(UsersService::class.java)
    }
}