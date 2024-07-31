package com.example.simplemediasearchapp.model

data class ResponseData<T>(
    val meta: Meta,
    val documents: List<T>
) {

    data class Meta(
        val totalCount: Int,
        val pageableCount: Int,
        val isEnd: Boolean
    )
}
