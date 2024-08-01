package com.example.simplemediasearchapp.network

import android.util.Log
import com.example.simplemediasearchapp.model.ImageData
import com.example.simplemediasearchapp.model.ResponseData
import com.example.simplemediasearchapp.model.VideoData
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("image")
    suspend fun getImages(
        @Query("query") query: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ResponseData<ImageData>

    @GET("vclip")
    suspend fun getVideos(
        @Query("query") query: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ResponseData<VideoData>
}

object NetworkManager {
    private val TAG = "NetworkManager"
    private const val BASE_URL = "https://dapi.kakao.com/v2/search/"

    private const val DEFAULT_LIST_SIZE = 10

    val logger = HttpLoggingInterceptor().apply { level = Level.BASIC }
    private val client = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor())
        .addInterceptor(logger)
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    suspend fun getImageList(query: String, page: Int): ResponseData<ImageData> {
        return retrofit.getImages(
                query = query,
                sort = SortType.recency.toString(),
                page = page,
                size = DEFAULT_LIST_SIZE
            )
    }

    suspend fun getVideoList(query: String, page: Int): ResponseData<VideoData> {
        return retrofit.getVideos(
                query = query,
                sort = SortType.recency.toString(),
                page = page,
                size = DEFAULT_LIST_SIZE
            )
    }
}

enum class SortType {
    recency, accuracy
}

class TokenInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        //테스트용 앱이라 하드코딩합니다
        builder.addHeader("Authorization"
            , "KakaoAK 43b5cac2302138afe74a4a7904bf425f")
        return chain.proceed(builder.build())
    }
}