package com.example.simplemediasearchapp.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplemediasearchapp.model.Media
import com.example.simplemediasearchapp.model.Meta
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
    val mediaList = MutableLiveData<List<Media>>()
    var mediaListInfo: MediaListInfo? = null

    fun search(query: String) {
        viewModelScope.launch {
            val imageDeferred = async {
                NetworkManager.getImageList("테스트", 1)
            }
//            val videoDeferred = async {
//                NetworkManager.getVideoList("테스트", 1)
//            }
            val imageData = imageDeferred.await()
            mediaList.value = imageData.documents.map { it.toMedia() }
        }
    }
}