package rocks.newsie.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
    onGotoSettings: () -> Unit
) {
    composable("home") {
        HomeScreen(
            onGotoSettings = onGotoSettings,
        )
    }
}

fun NavController.navigateToHome() {
    this.navigate("home")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onGotoSettings: () -> Unit = {},
) {
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
                    IconButton(onClick = onGotoSettings) {
                        Icon(Icons.Rounded.Settings, "Open the menu")
                    }
                }
            )
        },
        content = {
            Text("home", modifier = Modifier.padding(it))
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