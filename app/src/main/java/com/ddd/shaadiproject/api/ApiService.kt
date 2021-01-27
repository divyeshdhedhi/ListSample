package com.ddd.shaadiproject.api

import com.ddd.shaadiproject.data.User
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    fun getUsers(): Call<MutableList<User>>
}