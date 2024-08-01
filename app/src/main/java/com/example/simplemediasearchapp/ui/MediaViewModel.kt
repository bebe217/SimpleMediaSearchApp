package com.example.simplemediasearchapp.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private var mediaListInfo: MediaListInfo? = null

    fun search(query: String) {
        println("search $query")
        val page = 1
        viewModelScope.launch {
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
        }
    }

    private fun appendMediaList(imageData: ResponseData<ImageData>, videoData: ResponseData<VideoData>) {
        val list = mutableListOf<Media>()
        list.addAll(imageData.documents.map { it.toMedia() })
        list.addAll(videoData.documents.map { it.toMedia() })
        list.sortByDescending { it.datetime }
        mediaList.clear()
        mediaList.addAll(list)
    }
}