package rocks.newsie.app.ui.partials

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * State for the [AppBar] composable
 */
@Stable
class AppBarState(
    var title: String = "Title",
    var navIcon: ImageVector = Icons.Default.Menu,
    var onNavIconClick: () -> Unit = {}
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    state: AppBarState,
) {
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        navigationIcon = {
            IconButton(onClick = state.onNavIconClick) {
                Icon(state.navIcon, "Click the NAV icon")
            }
        },
        title = {
            Text(state.title)
        },
        actions = {
            IconButton(onClick = {/* Do Something*/ }) {
                Icon(Icons.Filled.Share, null)
            }
        })
}

