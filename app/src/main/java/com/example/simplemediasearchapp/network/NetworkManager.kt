package com.example.simplemediasearchapp.network

import android.util.Log
import com.example.simplemediasearchapp.model.ImageData
import com.example.simplemediasearchapp.model.ResponseData
import com.example.simplemediasearchapp.model.VideoData
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
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

    private val client = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor())
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    suspend fun getImageList(query: String, page: Int) {
        try {
            val result = retrofit.getImages(
                query = query,
                sort = "recency",
                page = page,
                size = 20
            )
            Log.d(TAG, "getImageList: $result")
        } catch (e: Exception) {
            Log.e(TAG, "getImageList: ", e)
        }
    }

    suspend fun getVideoList(query: String, page: Int) {
        try {
            val result = retrofit.getVideos(
                query = query,
                sort = "recency",
                page = page,
                size = 20
            )
            Log.d(TAG, "getVideoList: $result")
        } catch (e: Exception) {
            Log.e(TAG, "getVideoList: ", e)
        }
    }
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