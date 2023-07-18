package rocks.newsie.app.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import rocks.newsie.app.ui.theme.AppTheme

fun NavGraphBuilder.homeScreen(
    onOpenDrawer: () -> Unit
) {
    composable("home") {
        HomeScreen(
            onOpenDrawer = onOpenDrawer,
        )
    }
}

fun NavController.navigateToHome() {
    this.navigate("home")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onOpenDrawer: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Rounded.Menu, "Open the menu")
                    }
                },
                title = {},
                actions = {}
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
    AppTheme() {
        HomeScreen()
    }
}