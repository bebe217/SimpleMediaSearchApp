package com.example.simplemediasearchapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.simplemediasearchapp.model.Media
import com.example.simplemediasearchapp.ui.theme.SimpleMediaSearchAppTheme

@Composable
fun MediaScreen(viewModel: MediaViewModel, modifier: Modifier = Modifier) {
    Column {
        Spacer(Modifier.height(16.dp))
        SearchBar(Modifier.padding(horizontal = 10.dp)) { text ->
            viewModel.search(text)
        }
        MediaList(viewModel, modifier)
    }
}

@Composable
fun MediaList(viewModel: MediaViewModel, modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()

    val reachedBottom: Boolean by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index != 0 && lastVisibleItem?.index == (listState.layoutInfo.totalItemsCount - 1)
        }
    }

    LaunchedEffect(reachedBottom) {
        println("reachedBottom $reachedBottom")
        if (reachedBottom) viewModel.loadMore()
    }

    LazyColumn (
        modifier = modifier,
        state = listState
    ) {
        items(viewModel.mediaList) { item ->
            MediaItem(item) {
                viewModel.favorite(it)
            }
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearchAction: (text: String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        value = text,
        onValueChange = { text = it },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            onSearchAction(text)
            keyboardController?.hide()
        }),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
    )
}

@Composable
fun MediaItem(item: Media, onClick: (item: Media) -> Unit) {
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
                Icon(
                    imageVector =
                    if (item.favorite) Icons.Default.Favorite
                    else Icons.Default.FavoriteBorder
                    ,
                    contentDescription = null,
                )
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
fun MediaPreview() {
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
                    MediaItem(item) {}
                }
            }
        }
    }
}