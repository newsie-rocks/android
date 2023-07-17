package rocks.newsie.app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import rocks.newsie.app.ui.partials.AppBar
import rocks.newsie.app.ui.partials.AppBarState
import rocks.newsie.app.ui.partials.Drawer
import rocks.newsie.app.ui.screens.FeedScreen
import rocks.newsie.app.ui.screens.HomeScreen
import rocks.newsie.app.ui.screens.SettingsScreen
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

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val appBarState by remember {
        derivedStateOf {
            when (val route = navBackStackEntry?.destination?.route) {
                "home" -> {
                    AppBarState(
                        title = "News",
                        navIcon = Icons.Rounded.Menu,
                        onNavIconClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }
                    )
                }

                else -> {
                    Log.i("Root", "route: $route")
                    val title = when (route) {
                        "settings" -> "Settings"
                        else -> "NA"
                    }
                    AppBarState(
                        title = title,
                        navIcon = Icons.Rounded.ArrowBack,
                        onNavIconClick = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Drawer(
                    navigateTo = {
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(it)
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                AppBar(state = appBarState)
            },
            content = { innerPadding ->
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(modifier = Modifier.padding(innerPadding))
                    }
                    composable(
                        "feed/{feedId}",
                        arguments = listOf(navArgument("feedId") { type = NavType.StringType })
                    ) {
                        FeedScreen(
                            modifier = Modifier.padding(innerPadding),
                            feedId = it.arguments?.getString("feedId").toString()
                        )
                    }
                    composable("settings") {
                        SettingsScreen(modifier = Modifier.padding(innerPadding))
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