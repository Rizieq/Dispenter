package com.intech.data.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UsersService {
    @GET("/register.php")
    fun register(
        @Query("val") userData: String
    ): Call<String>

    @GET("/login.php")
    fun login(
        @Query("val") userData: String
    ): Call<String>

    @GET("/about.php")
    fun profile(
        @Query("val") userData: String
    ): Call<String>

    @GET("/reset.php")
    fun resetPassword(
        @Query("val") email: String
    ): Call<String>
}