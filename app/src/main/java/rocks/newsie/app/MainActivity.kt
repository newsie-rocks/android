package rocks.newsie.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import rocks.newsie.app.data.FeedRepository
import rocks.newsie.app.data.SettingsStore
import rocks.newsie.app.domain.FeedsUseCase
import rocks.newsie.app.ui.screens.articleScreen
import rocks.newsie.app.ui.screens.feedScreen
import rocks.newsie.app.ui.screens.homeScreen
import rocks.newsie.app.ui.screens.settingsScreen
import rocks.newsie.app.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Root()
                }
            }
        }
    }
}

@Composable
fun Root() {
    val navController: NavHostController = rememberNavController()
    val context = LocalContext.current
    val settingsStore = SettingsStore(context = context)
    val feedRepository = FeedRepository(context)
    val feedsUseCase = FeedsUseCase(feedRepository)

    NavHost(
        navController = navController,
        startDestination = "home",
    ) {
        homeScreen(navController, feedsUseCase)
        feedScreen(navController, feedsUseCase)
        articleScreen(navController)
        settingsScreen(navController, settingsStore)
    }
}

@Preview(showBackground = true)
@Composable
fun RootPreview() {
    AppTheme {
        Root()
    }
}