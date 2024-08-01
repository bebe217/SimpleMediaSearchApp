package com.example.simplemediasearchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simplemediasearchapp.ui.FavoriteScreen
import com.example.simplemediasearchapp.ui.MediaScreen
import com.example.simplemediasearchapp.ui.MediaViewModel
import com.example.simplemediasearchapp.ui.theme.SimpleMediaSearchAppTheme

data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)
class MainActivity : ComponentActivity() {
    private val viewModel: MediaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.dataStorage = DataStorage(this)
        viewModel.loadFavList()

        setContent {
            val homeTab = TabBarItem(
                title = "Home",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home
            )
            val favTab = TabBarItem(
                title = "Favorite",
                selectedIcon = Icons.Filled.Favorite,
                unselectedIcon = Icons.Outlined.FavoriteBorder
            )

            val tabBarItems = listOf(homeTab, favTab)
            val navController = rememberNavController()

            SimpleMediaSearchAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { TabView(tabBarItems, navController) }) { innerPadding ->
                    NavHost(navController = navController, startDestination = homeTab.title) {
                        composable(homeTab.title) {
                            MediaScreen(
                                viewModel = viewModel,
                                modifier = Modifier.padding(innerPadding).fillMaxWidth()
                            )
                        }
                        composable(favTab.title) {
                            FavoriteScreen(
                                viewModel = viewModel,
                                modifier = Modifier.padding(innerPadding).fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun TabView(tabBarItems: List<TabBarItem>, navController: NavController) {
        var selectedTabIndex by rememberSaveable {
            mutableIntStateOf(0)
        }

        NavigationBar {
            tabBarItems.forEachIndexed { index, tabBarItem ->
                NavigationBarItem(
                    selected = selectedTabIndex == index,
                    onClick = {
                        selectedTabIndex = index
                        navController.navigate(tabBarItem.title)
                    },
                    icon = {
                        Icon(
                            imageVector = if (selectedTabIndex == index) {
                                tabBarItem.selectedIcon
                            } else {
                                tabBarItem.unselectedIcon
                            },
                            contentDescription = tabBarItem.title
                        )
                    },
                    label = { Text(tabBarItem.title) })
            }
        }
    }

}