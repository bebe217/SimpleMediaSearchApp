package com.example.simplemediasearchapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.simplemediasearchapp.model.Media
import com.example.simplemediasearchapp.ui.theme.SimpleMediaSearchAppTheme

@Composable
fun FavoriteScreen(viewModel: MediaViewModel, modifier: Modifier = Modifier) {
    Column {
        FavList(viewModel, modifier)
    }
}

@Composable
fun FavList(viewModel: MediaViewModel, modifier: Modifier = Modifier) {
    LazyColumn (
        modifier = modifier,
    ) {
        items(viewModel.favList) { item ->
            FavItem(item) {
                viewModel.unfavorite(it)
            }
        }
    }
}

@Composable
fun FavItem(item: Media, onClick: (item: Media) -> Unit) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(7.dp)
            .clickable {
                onClick(item)
            }
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
                modifier = Modifier
                    .fillMaxHeight()
                    .width(200.dp)
            )
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.weight(1F))
                Text(
                    text = item.datetime,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavPreview() {
    val mediaList = listOf(
        Media(
            thumbnailUrl = "https://search3.kakaocdn.net/argon/130x130_85_c/2VhUMKQRSTf",
            datetime = "2024-08-01T12:41:55.000+09:00"
        ),
        Media(
            thumbnailUrl = "https://search3.kakaocdn.net/argon/130x130_85_c/3YlTYmz58I1",
            datetime = "2024-08-01T12:34:44.000+09:00"
        )
    )
    SimpleMediaSearchAppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            LazyColumn (
                modifier = Modifier.padding(innerPadding)
            ) {
                items(mediaList) { item ->
                    FavItem(item) {}
                }
            }
        }
    }
}