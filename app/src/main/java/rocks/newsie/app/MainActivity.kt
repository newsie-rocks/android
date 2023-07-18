package rocks.newsie.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import rocks.newsie.app.ui.partials.Drawer
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
fun Root(
    navController: NavHostController = rememberNavController(),
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp),
            ) {
                Drawer(
                    onMenuItemClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navController.navigate(it)
                    },
                )
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = "home",
        ) {
            homeScreen(
                onOpenDrawer = {
                    coroutineScope.launch {
                        drawerState.open()
                    }
                }
            )
            feedScreen()
            settingsScreen(
                onOpenDrawer = {
                    coroutineScope.launch {
                        drawerState.open()
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RootPreview() {
    AppTheme {
        Root()
    }
}