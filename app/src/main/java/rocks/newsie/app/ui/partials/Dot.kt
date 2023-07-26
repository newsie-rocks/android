package rocks.newsie.app.ui.partials

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import rocks.newsie.app.ui.theme.AppTheme

@Composable
fun Dot(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Dp = 12.dp,
) {
    Canvas(
        modifier = modifier.size(size),
        onDraw = {
            drawCircle(color = color)
        })
}

@Preview(showBackground = false)
@Composable
fun DotPreview() {
    AppTheme {
        Dot(
            color = Color.Red,
            size = 12.dp,
        )
    }
}
