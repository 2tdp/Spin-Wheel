package com.spin.wheel.chooser.wheeloffortune.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("data/{id}")
    fun getData(
        @Path("id") id: Int,
        @Query("name") name: String): String
}