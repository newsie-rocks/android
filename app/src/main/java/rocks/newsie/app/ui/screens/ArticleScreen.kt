package rocks.newsie.app.ui.screens

import android.os.Parcelable
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.parcelize.Parcelize
import rocks.newsie.app.ui.theme.AppTheme

fun NavGraphBuilder.articleScreen(
    navController: NavController,
) {
    composable("articles/{articleUrl}") {
        val articleUrl = it.arguments?.getString("articleUrl").toString()
        val viewModel = rememberArticleScreenViewModel(
            navController,
            articleUrl
        )
        ArticleScreen(viewModel = viewModel)
    }
}

fun NavController.navigateToArticle(articleUrl: String) {
    this.navigate("articles/$articleUrl")
}

class ArticleScreenViewModel(
    private val navController: NavController,
    val articleUrl: String,
) {
    fun onGoBack() {
        navController.popBackStack()
    }
}

@Parcelize
data class ArticleScreenViewModelHolder(val articleUrl: String) : Parcelable

@Composable
fun rememberArticleScreenViewModel(
    navController: NavController,
    articleUrl: String,
): ArticleScreenViewModel {
    return rememberSaveable(saver = Saver(
        save = {
            ArticleScreenViewModelHolder(articleUrl = it.articleUrl)
        },
        restore = {
            ArticleScreenViewModel(
                navController = navController,
                articleUrl = it.articleUrl,
            )
        }
    )) {
        ArticleScreenViewModel(
            navController,
            articleUrl,
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleScreen(
    modifier: Modifier = Modifier,
    viewModel: ArticleScreenViewModel,
) {
    val articleUrl = viewModel.articleUrl
    // Declare a string that contains a url
    val mUrl = "https://www.geeksforgeeks.org"

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { viewModel.onGoBack() }) {
                        Icon(Icons.Rounded.ArrowBack, "Go back")
                    }
                },
                title = {
                    Text("Article")
                },
            )
        },
        content = { innerPadding ->
            // Adding a WebView inside AndroidView
            // with layout as full screen
            AndroidView(
                modifier = Modifier.padding(innerPadding),
                factory = {
                    WebView(it).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        webViewClient = WebViewClient()
                        loadUrl(mUrl)
                    }
                },
                update = {
                    it.loadUrl(mUrl)
                }
            )
        }
    )
}

@Preview(showBackground = false)
@Composable
fun ArticleScreenPreview() {
    val navController = rememberNavController()
    val articleUrl = ""
    val viewModel = rememberArticleScreenViewModel(navController, articleUrl)

    AppTheme {
        ArticleScreen(viewModel = viewModel)
    }
}