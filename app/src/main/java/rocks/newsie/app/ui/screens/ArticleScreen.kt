package rocks.newsie.app.ui.screens

import android.content.Intent
import android.os.Parcelable
import android.text.util.Linkify
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Web
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
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
    var isMenuOpened by mutableStateOf(false)
        private set

    fun onGoBack() {
        navController.popBackStack()
    }

    fun openMenu() {
        isMenuOpened = true
    }

    fun closeMenu() {
        isMenuOpened = false
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
    val content = remember(key1 = article) {
        article?.content?.trim()
    }
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current

    // share sheet
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { viewModel.onGoBack() }) {
                        Icon(Icons.Rounded.ArrowBack, "Go back")
                    }
                },
                title = {
                    Text(
                        "${article?.title}",
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.openMenu() }) {
                        Icon(Icons.Rounded.MoreVert, "Open article menu")
                    }
                    DropdownMenu(
                        expanded = viewModel.isMenuOpened,
                        onDismissRequest = { viewModel.closeMenu() }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(Icons.Rounded.Web, "Open the article in the browser")
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("Open")
                                }

                            },
                            onClick = {
                                uriHandler.openUri(article?.link ?: "")
                                viewModel.closeMenu()
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(Icons.Rounded.Share, "Share icon")
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("Share")
                                }

                            },
                            onClick = {
                                context.startActivity(shareIntent)
                                viewModel.closeMenu()
                            }
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(32.dp)
                    .fillMaxHeight()
                    .verticalScroll(scrollState),
            ) {
                Text(
                    "${article?.title}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        uriHandler.openUri(article?.link ?: "")
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(article?.pubDate.toString(), fontSize = 12.sp)
                }
                if (!content.isNullOrEmpty()) {
                    HtmlView(
                        modifier = Modifier.padding(innerPadding),
                        html = content,
                    )
                } else {
                    LinkView(
                        modifier = Modifier.padding(innerPadding),
                        url = article?.link ?: "",
                    )
                }
            }
        }
    )
}

@Composable
private fun HtmlView(
    modifier: Modifier = Modifier,
    html: String,
) {
    val spannedText = remember(key1 = html) {
        HtmlCompat.fromHtml(html, 0)
    }

    AndroidView(
        modifier = modifier,
        factory = {
            TextView(it).apply {
                textSize = 16f
                setLineSpacing(0f, 1f)
                autoLinkMask = Linkify.WEB_URLS
                linksClickable = true
            }
        },
        update = { it.text = spannedText },
    )
}

@Composable
private fun LinkView(
    modifier: Modifier = Modifier,
    url: String,
) {
    AndroidView(
        modifier = modifier,
        factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = WebViewClient()
                loadUrl(url)
            }
        },
        update = {
            it.loadUrl(url)
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