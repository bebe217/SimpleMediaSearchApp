package com.example.simplemediasearchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.simplemediasearchapp.model.Media
import com.example.simplemediasearchapp.ui.MediaViewModel
import com.example.simplemediasearchapp.ui.theme.SimpleMediaSearchAppTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MediaViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.search("테스트")
        setContent {
            SimpleMediaSearchAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MediaScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MediaScreen(viewModel: MediaViewModel, modifier: Modifier = Modifier) {
    val mediaList by viewModel.mediaList.observeAsState(emptyList())

    LazyColumn (
        modifier = modifier
    ) {
        items(mediaList) { item ->
            MediaItem(item)
        }
    }
}

@Composable
fun MediaItem(item: Media) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(7.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.thumbnailUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxHeight().width(200.dp)
            )
            Text(
                text = item.datetime,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.height(60.dp).padding(horizontal = 16.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MediaPreview() {
    SimpleMediaSearchAppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            LazyColumn (
                modifier = Modifier.padding(innerPadding)
            ) {
//                items(mediaTempList) { item ->
//                    MediaItem(item)
//                }
            }
        }
    }
}