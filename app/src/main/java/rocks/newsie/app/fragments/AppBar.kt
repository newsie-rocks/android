package rocks.newsie.app.fragments

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(onNavIconClick: () -> Unit) {
    TopAppBar(
        title = {
            Text("News")
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        navigationIcon = {
            IconButton(onClick = onNavIconClick) {
                Icon(Icons.Default.Menu, "Toggle drawer")
            }
        },
        actions = {
            IconButton(onClick = {/* Do Something*/ }) {
                Icon(Icons.Filled.Share, null)
            }
            IconButton(onClick = {/* Do Something*/ }) {
                Icon(Icons.Filled.Settings, null)
            }
        })
}

