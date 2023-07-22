package rocks.newsie.app.ui.partials

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rocks.newsie.app.R
import rocks.newsie.app.ui.theme.AppTheme
import rocks.newsie.app.ui.theme.frenchCannonFontFamily

@Composable
fun Logo() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.logo_grad),
            contentDescription = "Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(42.dp)
        )
        Spacer(Modifier.size(16.dp))
        Text(
            "Newsie",
            fontFamily = frenchCannonFontFamily,
            fontSize = 32.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LogoPreview() {
    AppTheme {
        Logo()
    }
}