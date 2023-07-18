package rocks.newsie.app.ui.partials

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rocks.newsie.app.R
import rocks.newsie.app.ui.theme.AppTheme
import rocks.newsie.app.ui.theme.frenchCannonFontFamily

private data class MenuItem(val label: String, val link: String, val icon: ImageVector)

@Composable
fun Drawer(
    modifier: Modifier = Modifier,
    onMenuItemClick: (id: String) -> Unit = {},
) {
    val menus = listOf(
        MenuItem(
            label = "Home",
            link = "home",
            icon = Icons.Rounded.Home
        ),
        MenuItem(
            label = "Settings",
            link = "settings",
            icon = Icons.Rounded.Settings
        )
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 32.dp,
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_grad),
                contentDescription = "Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(Modifier.size(16.dp))
            Text(
                text = "Newsie",
                fontFamily = frenchCannonFontFamily,
                fontSize = 24.sp
            )
        }

        Divider()
        menus.forEach {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onMenuItemClick(it.link) }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 18.dp,
                    )
                ) {
                    Icon(
                        it.icon,
                        contentDescription = "Menu icon",
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    Text(it.label)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DrawerPreview() {
    AppTheme {
        Drawer(onMenuItemClick = {})
    }
}