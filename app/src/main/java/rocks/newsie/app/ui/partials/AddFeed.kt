package rocks.newsie.app.ui.partials

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rocks.newsie.app.ui.theme.AppTheme

@Composable
fun AddFeed(
    modifier: Modifier = Modifier,
    onSubmit: (url: String, name: String?) -> Unit,
    error: String? = null,
) {
    var url by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
    ) {
        Text(
            "Add a new feed",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text("URL") },
            value = url,
            onValueChange = { url = it },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            label = { Text("Name (optional)") },
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onSubmit(url, name) },
            modifier = Modifier.height(52.dp),
        ) {
            Text("Add a feed", modifier = Modifier.fillMaxWidth())
        }
        if (error != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                error,
                color = Color.Red,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddFeedPreview() {
    AppTheme {
        AddFeed(
            onSubmit = { _, _ -> },
        )
    }
}