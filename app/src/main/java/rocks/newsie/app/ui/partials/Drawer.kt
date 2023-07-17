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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import rocks.newsie.app.ui.theme.AppTheme

@Composable
fun Drawer(
    modifier: Modifier = Modifier,
    navigateTo: (String) -> Unit
) {
    val rowModifier = Modifier
        .fillMaxWidth()
    val rowTextModifier = Modifier
        .padding(12.dp)

    Column(modifier = modifier) {
        Text(text = "Header", modifier = rowTextModifier)
        Divider()
        Row(modifier = rowModifier
            .clickable {
                navigateTo("home")
            }) {
            Text("Home", modifier = rowTextModifier)
        }
        Row(modifier = rowModifier
            .clickable {
                navigateTo("settings")
            }) {
            Text("Settings", modifier = rowTextModifier)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DrawerPreview() {
    AppTheme {
        Drawer(navigateTo = {})
    }
}