package com.waterhub.data.service

import com.waterhub.model.Banner
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

import retrofit2.http.GET


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
    fun
            profile(
        @Query("val") userData: String
    ): Call<String>

    @GET("/reset.php")
    fun resetPassword(
        @Query("val") email: String
    ): Call<String>

    @GET("/edit.php")
    fun editProfile(
        @Query("val") userData: String
    ): Call<String>


    @FormUrlEncoded
    @POST("/getbanner.php")
    fun getBanners(
        @Field("gender") gender: String
    ): Call<List<Banner>>


}