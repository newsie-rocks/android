package rocks.newsie.app.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FeedScreen(modifier: Modifier = Modifier, feedId: String) {
    Text("feed $feedId", modifier = modifier)
}