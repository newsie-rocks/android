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
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import rocks.newsie.app.data.FeedCollection
import rocks.newsie.app.fragments.AppBar
import rocks.newsie.app.fragments.Drawer
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
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val collections = listOf(
        FeedCollection(name = "feed 1", feeds = listOf()),
        FeedCollection(name = "feed 2", feeds = listOf())
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Drawer(collections = collections, onClickCollection = {
                    Log.d("Root", "onClickCollection clicked")
                }, modifier = Modifier)
            }
        },
        content = {
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
                    Greeting("nick", modifier = Modifier.padding(paddingValues))
                }
            )
        }
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun RootPreview() {
    AppTheme {
        Root()
    }
}