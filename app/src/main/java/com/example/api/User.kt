package com.example.api

data class User(
    val id: Int,
    val username: String,
    val password: String,

    )

data class Problema(
        val id: Int,
        val lat: String,
        val lon: String,
        val user_id: Int,
        val descr: String
)