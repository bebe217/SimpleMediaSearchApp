package com.example.simplemediasearchapp.model

import com.google.gson.annotations.SerializedName

data class ImageData(
    val collection: String,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String,
    @SerializedName("image_url")
    val imageUrl: String,
    val width: Long,
    val height: Long,
    @SerializedName("display_sitename")
    val displaySitename: String,
    val docUrl: String,
    val datetime: String,
)
