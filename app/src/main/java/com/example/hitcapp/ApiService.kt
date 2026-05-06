package com.example.hitcapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("products")
    fun getProducts(): Call<List<Product>>

    @GET("products/{id}")
    fun getProductDetail(@Path("id") id: Int): Call<Product>

    @GET("notifications")
    fun apiGetNotifications(): Call<List<com.example.hitcapp.Notification>>
}