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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import rocks.newsie.app.domain.Feed
import rocks.newsie.app.ui.theme.AppTheme

@Composable
fun AddFeed(
    modifier: Modifier = Modifier,
    onSubmit: (Feed) -> Unit,
) {
    var url by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
    ) {
        Text("Add a new feed")
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            label = { Text("URL") },
            value = url,
            onValueChange = { url = it },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            label = { Text("Name") },
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            onSubmit(
                Feed(
                    id = "",
                    url = url,
                    name = name,
                )
            )
        }) {
            Text("Add a feed", modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddFeedPreview() {
    AppTheme {
        AddFeed(
            onSubmit = {}
        )
    }
}