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


fun NavGraphBuilder.settingsScreen(
    onOpenDrawer: () -> Unit
) {
    composable("settings") {
        SettingsScreen(
            onOpenDrawer = onOpenDrawer,
        )
    }
}

fun NavController.navigateToSettings() {
    this.navigate("settings")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
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
                title = {
                    Text("Settings")
                },
                actions = {}
            )
        },
        content = {
            Text("Settings", modifier = Modifier.padding(it))
        }
    )
}

@Preview(showBackground = false)
@Composable
fun SettingsScreenPreview() {
    AppTheme {
        SettingsScreen()
    }
}