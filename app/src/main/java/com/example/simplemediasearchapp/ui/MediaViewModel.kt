package com.example.simplemediasearchapp.ui

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplemediasearchapp.DataStorage
import com.example.simplemediasearchapp.model.ImageData
import com.example.simplemediasearchapp.model.Media
import com.example.simplemediasearchapp.model.Meta
import com.example.simplemediasearchapp.model.ResponseData
import com.example.simplemediasearchapp.model.VideoData
import com.example.simplemediasearchapp.network.NetworkManager
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MediaListInfo(
    var query: String,
    var page: Int = 1,
    var imageMeta: Meta,
    var videoMeta: Meta
)

class MediaViewModel: ViewModel() {
    var mediaList = mutableStateListOf<Media>()
        private set
    var favList = mutableStateListOf<Media>()
        private set
    private var mediaListInfo: MediaListInfo? = null
    private var isLoading = false

    lateinit var dataStorage: DataStorage

    fun search(query: String) {
        println("search $query")
        if (isLoading) return
        val page = 1
        viewModelScope.launch {
            isLoading = true
            val imageDeferred = async {
                NetworkManager.getImageList(query, page)
            }
            val videoDeferred = async {
                NetworkManager.getVideoList(query, page)
            }
            val imageData = imageDeferred.await()
            val videoData = videoDeferred.await()
            mediaListInfo = MediaListInfo(query, page, imageData.meta, videoData.meta)
            mediaList.clear()
            appendMediaList(imageData, videoData)
            isLoading = false
        }
    }

    fun loadMore() {
        println("loadMore")
        mediaListInfo?.let {
            if (isLoading) return
            if (it.imageMeta.isEnd || it.videoMeta.isEnd) return
            viewModelScope.launch {
                isLoading = true
                val page = it.page + 1
                val imageDeferred = async {
                    NetworkManager.getImageList(it.query, page)
                }
                val videoDeferred = async {
                    NetworkManager.getVideoList(it.query, page)
                }
                val imageData = imageDeferred.await()
                val videoData = videoDeferred.await()
                mediaListInfo = MediaListInfo(it.query, page, imageData.meta, videoData.meta)
                appendMediaList(imageData, videoData)
                isLoading = false
            }
        }
    }

    private fun appendMediaList(imageData: ResponseData<ImageData>, videoData: ResponseData<VideoData>) {
        val list = mutableListOf<Media>()
        list.addAll(imageData.documents.map { it.toMedia() })
        list.addAll(videoData.documents.map { it.toMedia() })
        list.sortByDescending { it.datetime }
        mediaList.addAll(list)
    }

    fun loadFavList() {
        favList.addAll(dataStorage.getArrayList())
    }

    fun favorite(media: Media) {
        println("favorite ${media.thumbnailUrl}")
        favList.add(media)
        dataStorage.saveArrayList(favList.toList())
    }

    fun unfavorite(media: Media) {
        println("unfavorite ${media.thumbnailUrl}")
        favList.remove(media)
        dataStorage.saveArrayList(favList.toList())
    }

}