package com.example.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

    @GET("/mySlim/api/problemas")
    fun getProblema():Call<List<Problema>>

    @FormUrlEncoded
    @POST("/mySlim/api/adicionarmarcador")
    fun postMarker(
    @Field("latitude") lat: String?,
    @Field("longitude") lon: String?,
    @Field("descr") descr: String?,
    @Field("user_id") user_id: Int): Call<OutputPost>


    @FormUrlEncoded
    @POST("/mySlim/api/user")
    fun loginPost(
            @Field("user") user: String?,
            @Field("password") password: String?): Call<OutputPost>
}


