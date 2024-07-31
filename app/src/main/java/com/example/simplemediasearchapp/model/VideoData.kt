package com.example.simplemediasearchapp.model

import com.google.gson.annotations.SerializedName

data class VideoData(
    val title: String,
    @SerializedName("play_time")
    val playTime: Long,
    val thumbnail: String,
    val url: String,
    val datetime: String,
    val author: String,
)