package rocks.newsie.app.fragments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import rocks.newsie.app.data.FeedCollection

@Composable
fun Drawer(
    modifier: Modifier,
    collections: List<FeedCollection>,
    onGotoClick: (String) -> Unit,
) {
    Column {
        Text(text = "Header", modifier = modifier.padding(32.dp))
        Column(modifier) {
            Row(modifier = Modifier.clickable {
                onGotoClick("home")
            }) {
                Text("Home")
            }
            collections.forEach { collection ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onGotoClick("feed/${collection.id}")
                    }) {
                    Text("collection ${collection.name}")
                }
            }
        }
    }
}
