package rocks.newsie.app.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable


fun NavGraphBuilder.feedScreen() {
    composable("feeds/{feedId}") {
        val feedId = it.arguments?.getString("feedId").toString()
        FeedScreen(feedId = feedId)
    }
}

fun NavController.navigateToFeed(feedId: String) {
    this.navigate("feeds/$feedId")
}

@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    feedId: String
) {
    Text("feed $feedId", modifier = modifier)
}