package com.khuram.dogs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.khuram.dogs.dataStore.SettingsDataStore
import com.khuram.dogs.presentation.navigation.Screen
import com.khuram.dogs.presentation.ui.breed_images.BreedImageScreen
import com.khuram.dogs.presentation.ui.breed_images.BreedImagesScreen
import com.khuram.dogs.presentation.ui.breed_images.BreedImagesViewModel
import com.khuram.dogs.presentation.ui.breed_list.BreedListScreen
import com.khuram.dogs.presentation.ui.breed_list.BreedListViewModel
import com.khuram.dogs.presentation.ui.breed_list.FavBreedListScreen
import com.khuram.dogs.presentation.ui.breed_list.SubBreedListScreen
import com.khuram.dogs.util.ConnectionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var connectionManager: ConnectionManager

    @Inject
    lateinit var settingsDataStore: SettingsDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        connectionManager.registerConnectionObserver(this)

        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Screen.BreedList.route
            ) {
                composable(
                    route = Screen.BreedList.route
                ) {
                    val viewModel = hiltViewModel<BreedListViewModel>()
                    BreedListScreen(
                        isDarkTheme = settingsDataStore.isDark.value,
                        isNetworkAvailable = connectionManager.isNetworkAvailable.value,
                        onToggleTheme = { settingsDataStore.toggleTheme() },
                        onNavigateTo = navController::navigate,
                        viewModel = viewModel
                    )
                }
                composable(
                    route = Screen.SubBreedList.route + "/{breedName}",
                    arguments = listOf(
                        navArgument("breedName") { type = NavType.StringType }
                    )
                ) { navBackStackEntry ->
                    val viewModel: BreedListViewModel = hiltViewModel(navBackStackEntry)
                    SubBreedListScreen(
                        isDarkTheme = settingsDataStore.isDark.value,
                        isNetworkAvailable = connectionManager.isNetworkAvailable.value,
                        onToggleTheme = { settingsDataStore.toggleTheme() },
                        onNavigateTo = navController::navigate,
                        viewModel = viewModel,
                        breedName = navBackStackEntry.arguments?.getString("breedName")?: "",
                    )
                }
                composable(
                    route = Screen.BreedImageList.route + "/{breedName}?mainBreed={mainBreed}",
                    arguments = listOf(
                        navArgument("breedName") { type = NavType.StringType },
                        navArgument("mainBreed") {
                            defaultValue = ""
                            type = NavType.StringType
                        }
                    )
                ) { navBackStackEntry ->
                    val viewModel = hiltViewModel<BreedImagesViewModel>()
                    BreedImagesScreen(
                        isDarkTheme = settingsDataStore.isDark.value,
                        isNetworkAvailable = connectionManager.isNetworkAvailable.value,
                        onToggleTheme = { settingsDataStore.toggleTheme() },
                        viewModel = viewModel,
                        onNavigateTo = navController::navigate,
                        breedName = navBackStackEntry.arguments?.getString("breedName")?: "",
                        mainBreed = navBackStackEntry.arguments?.getString("mainBreed")?: ""
                    )
                }
                composable(
                    route = Screen.BreedImage.route + "/{image}",
                    arguments = listOf(
                        navArgument("image") { type = NavType.StringType }
                    )
                ) { navBackStackEntry ->
                    BreedImageScreen(
                        isDarkTheme = settingsDataStore.isDark.value,
                        isNetworkAvailable = connectionManager.isNetworkAvailable.value,
                        image = navBackStackEntry.arguments?.getString("image")?: ""
                    )
                }
                composable(
                    route = Screen.FavouriteBreeds.route
                ) { navBackStackEntry ->
                    val viewModel: BreedListViewModel = hiltViewModel(navBackStackEntry)
                    FavBreedListScreen(
                        isDarkTheme = settingsDataStore.isDark.value,
                        isNetworkAvailable = connectionManager.isNetworkAvailable.value,
                        onToggleTheme = { settingsDataStore.toggleTheme() },
                        onNavigateTo = navController::navigate,
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        connectionManager.registerConnectionObserver(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        connectionManager.unregisterConnectionObserver(this)
    }
}