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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.parcelize.Parcelize
import rocks.newsie.app.data.rememberFeedParser
import rocks.newsie.app.data.rememberFeedRepository
import rocks.newsie.app.domain.FeedsUseCase
import rocks.newsie.app.domain.rememberFeedsUseCase
import rocks.newsie.app.ui.theme.AppTheme

fun NavGraphBuilder.articleScreen(
    navController: NavController,
    feedsUseCase: FeedsUseCase,
) {
    composable("articles/{articleId}") {
        val articleId = it.arguments?.getString("articleId").toString()
        val viewModel = rememberArticleViewModel(
            navController,
            feedsUseCase,
            articleId
        )
        ArticleScreen(viewModel = viewModel)
    }
}

fun NavController.navigateToArticle(articleId: String) {
    this.navigate("articles/$articleId")
}

class ArticleViewModel(
    private val navController: NavController,
    private val feedsUseCase: FeedsUseCase,
    val articleId: String,
) {
    val article = feedsUseCase.getArticle(articleId)

    fun onGoBack() {
        navController.popBackStack()
    }
}

@Parcelize
data class ArticleViewModelSaver(val articleId: String) : Parcelable

@Composable
fun rememberArticleViewModel(
    navController: NavController,
    feedsUseCase: FeedsUseCase,
    articleId: String,
): ArticleViewModel {
    return rememberSaveable(saver = Saver(
        save = {
            ArticleViewModelSaver(articleId = it.articleId)
        },
        restore = {
            ArticleViewModel(
                navController = navController,
                feedsUseCase = feedsUseCase,
                articleId = it.articleId,
            )
        }
    )) {
        ArticleViewModel(
            navController,
            feedsUseCase,
            articleId,
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleScreen(
    viewModel: ArticleViewModel,
) {
    val article by viewModel.article.collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { viewModel.onGoBack() }) {
                        Icon(Icons.Rounded.ArrowBack, "Go back")
                    }
                },
                title = {
                    Text("${article?.title}")
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
                        loadUrl(article?.link ?: "")
                    }
                },
                update = {
                    it.loadUrl(article?.link ?: "")
                }
            )
        }
    )
}

@Preview(showBackground = false)
@Composable
fun ArticleScreenPreview() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val feedParser = rememberFeedParser(context)
    val feedRepository = rememberFeedRepository(context)
    val feedsUseCase = rememberFeedsUseCase(feedRepository, feedParser)
    val viewModel = rememberArticleViewModel(
        navController,
        feedsUseCase,
        articleId = ""
    )

    AppTheme {
        ArticleScreen(viewModel = viewModel)
    }
}