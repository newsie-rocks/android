package rocks.newsie.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import rocks.newsie.app.R
import rocks.newsie.app.ui.theme.AppTheme
import rocks.newsie.app.ui.theme.frenchCannonFontFamily

fun NavGraphBuilder.homeScreen(
    onGotoSettings: () -> Unit,
) {
    composable("home") {
        HomeScreen(onGotoSettings)
    }
}

fun NavController.navigateToHome() {
    this.navigate("home")
}

@Composable
private fun rememberHomeScreenState(
    onGotoSettings: () -> Unit,
): HomeScreenState {
    return remember {
        HomeScreenState(onGotoSettings)
    }
}

@Stable
class HomeScreenState(
    val onGotoSettings: () -> Unit,
) {
    var value by mutableStateOf("abc")
        private set

    fun changeValue(v: String) {
        value = v
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onGotoSettings: () -> Unit = {},
) {
    val state = rememberHomeScreenState(onGotoSettings)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_grad),
                            contentDescription = "Logo",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(Modifier.size(16.dp))
                        Text(
                            "Newsie",
                            fontFamily = frenchCannonFontFamily,
                            fontSize = 20.sp
                        )
                    }

                },
                actions = {
                    IconButton(onClick = state.onGotoSettings) {
                        Icon(Icons.Rounded.Add, "Add a feed")
                    }
                    IconButton(onClick = state.onGotoSettings) {
                        Icon(Icons.Rounded.Settings, "Open the menu")
                    }
                }
            )
        },
        content = {
            Column(modifier = Modifier.padding(it)) {
                Text("Home")
                Text("value= ${state.value}")
                Button(onClick = { state.changeValue("XXXXXXXX") }) {
                    Text("Click me")
                }
            }
        }
    )
}

@Preview(showBackground = false)
@Composable
fun HomeScreenPreview() {
    AppTheme {
        HomeScreen()
    }
}