package rocks.newsie.app.ui.partials

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Drawer(
    modifier: Modifier = Modifier,
    navigateTo: (String) -> Unit
) {
    Column(modifier = modifier) {
        Text(text = "Header", modifier = Modifier.padding(32.dp))
        Divider()
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable {
                navigateTo("home")
            }) {
            Text("Home")
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable {
                navigateTo("settings")
            }) {
            Text("Settings")
        }
    }
}
