package com.example.simplemediasearchapp.model

import com.google.gson.annotations.SerializedName

data class ResponseData<T>(
    val meta: Meta,
    val documents: List<T>
) {
}

data class Meta(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("pageable_count")
    val pageableCount: Int,
    val isEnd: Boolean
)