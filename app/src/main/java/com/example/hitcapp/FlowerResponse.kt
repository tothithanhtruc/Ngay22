package com.example.hitcapp

import com.google.gson.annotations.SerializedName

data class FlowerResponse(
    @SerializedName("hits")
    val hits: List<PixabayFlower>
)

data class PixabayFlower(
    @SerializedName("id")
    val id: Int,
    @SerializedName("tags")
    val tags: String,
    @SerializedName("largeImageURL")
    val imageUrl: String,
    @SerializedName("webformatURL")
    val previewUrl: String
)