package rocks.newsie.app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import rocks.newsie.app.data.FeedCollection
import rocks.newsie.app.fragments.AppBar
import rocks.newsie.app.fragments.Drawer
import rocks.newsie.app.screens.FeedScreen
import rocks.newsie.app.screens.HomeScreen
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Root() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val collections = listOf(
        FeedCollection(id = "1", name = "feed 1", feeds = listOf()),
        FeedCollection(id = "2", name = "feed 2", feeds = listOf())
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Drawer(
                    collections = collections,
                    onGotoClick = { it ->
                        Log.d("Root", "clicked, go to: $it")
                        navController.navigate(it)
                        scope.launch {
                            drawerState.close()
                        }
                    }, modifier = Modifier
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                AppBar(onNavIconClick = {
                    scope.launch {
                        Log.d("Root", "nav icon clicked")
                        drawerState.open()
                    }
                })
            },
            content = { paddingValues ->
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
//                            Text("hey", modifier = Modifier.padding(paddingValues))
                        HomeScreen(modifier = Modifier.padding(paddingValues))
                    }
                    composable(
                        "feed/{feedId}",
                        arguments = listOf(navArgument("feedId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        FeedScreen(
                            modifier = Modifier.padding(paddingValues),
                            feedId = backStackEntry.arguments?.getString("feedId").toString()
                        )
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RootPreview() {
    AppTheme {
        Root()
    }
}