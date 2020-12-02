package com.example.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

    @FormUrlEncoded
    @POST("/mySlim/api/user")
    fun loginPost(
            @Field("user") user: String?,
            @Field("password") password: String?): Call<OutputPost>
}


