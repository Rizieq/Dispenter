package com.intech.networking

import com.intech.data.service.UsersService
import retrofit2.Retrofit
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import retrofit2.converter.scalars.ScalarsConverterFactory


class Api {
    companion object {
        private const val baseUrl = "http://api.waterhub.id"

        private fun retrofit(): Retrofit {
            val okHttpClient = OkHttpClient.Builder()
                .addNetworkInterceptor(StethoInterceptor())
                .build()

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
        }

        fun usersService(): UsersService = retrofit().create(UsersService::class.java)
    }
}