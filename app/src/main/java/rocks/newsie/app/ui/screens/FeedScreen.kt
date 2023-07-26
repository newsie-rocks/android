package rocks.newsie.app.ui.screens

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import rocks.newsie.app.data.rememberFeedParser
import rocks.newsie.app.data.rememberFeedRepository
import rocks.newsie.app.domain.Article
import rocks.newsie.app.domain.Feed
import rocks.newsie.app.domain.FeedsUseCase
import rocks.newsie.app.domain.rememberFeedsUseCase
import rocks.newsie.app.ui.partials.Dot
import rocks.newsie.app.ui.theme.AppTheme
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.feedScreen(
    navController: NavController,
    feedsUseCase: FeedsUseCase,
) {
    composable("feeds/{feedId}") {
        val feedId = it.arguments?.getString("feedId").toString()
        val viewModel = rememberFeedViewModel(
            navController,
            feedsUseCase,
            feedId,
        )
        FeedScreen(viewModel = viewModel)
    }
}

fun NavController.navigateToFeed(feedId: String) {
    this.navigate("feeds/$feedId")
}

class FeedViewModel(
    private val navController: NavController,
    private val feedsUseCase: FeedsUseCase,
    val feedId: String,
) {
    val feed = feedsUseCase.getFeed(feedId)
    val articles = feedsUseCase.getFeedArticles(feedId)

    fun onGoBack() {
        navController.popBackStack()
    }

    fun goToArticle(article: Article) {
        navController.navigateToArticle(article.id)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun refresh(feed: Feed) {
        feedsUseCase.loadArticles(feed)
    }
}

@Parcelize
data class FeedViewModelSaver(val feedId: String) : Parcelable

@Composable
fun rememberFeedViewModel(
    navController: NavController,
    feedsUseCase: FeedsUseCase,
    feedId: String,
): FeedViewModel {
    return rememberSaveable(saver = Saver(
        save = {
            FeedViewModelSaver(feedId = it.feedId)
        },
        restore = {
            FeedViewModel(
                navController = navController,
                feedsUseCase = feedsUseCase,
                feedId = it.feedId
            )
        }
    )) {
        FeedViewModel(
            navController,
            feedsUseCase,
            feedId,
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    viewModel: FeedViewModel,
) {
    val feed by viewModel.feed.collectAsState(initial = null)
    val articles by viewModel.articles.collectAsState(initial = listOf())
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { viewModel.onGoBack() }) {
                        Icon(Icons.Rounded.ArrowBack, "Go back")
                    }
                },
                title = {
                    Text("${feed?.name ?: feed?.title}")
                },
                actions = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            feed?.let {
                                viewModel.refresh(it)
                            }
                        }
                    }) {
                        Icon(Icons.Rounded.Refresh, "Refresh the feed")
                    }
                }
            )
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                items(articles) { article ->
                    ArticleRow(
                        article,
                        onGoToArticle = { viewModel.goToArticle(article) },
                    )
                    Divider()
                }
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ArticleRow(
    article: Article,
    onGoToArticle: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onGoToArticle()
            }
            .padding(vertical = 16.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // TODO: add the read tag on the article
        if (true) {
            Dot(
                color = Color(0xFF1D50F3),
                modifier = Modifier.padding(end = 20.dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                "${article.title}",
                fontSize = 16.sp,
                fontWeight = if (true) FontWeight.Bold else FontWeight.Normal
            )
            Spacer(Modifier.size(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                val formatter = remember { DateTimeFormatter.ofPattern("yyyy/dd/MM HH:mm:ss") }
                val pubDate = article.pubDate?.toLocalDateTime()
                Text("By: ${article.author}", fontSize = 12.sp)
                Text(pubDate?.format(formatter) ?: "", fontSize = 12.sp)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = false)
@Composable
fun FeedScreenPreview() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val feedParser = rememberFeedParser(context = context)
    val feedRepository = rememberFeedRepository(context = context)
    val feedsUseCase = rememberFeedsUseCase(feedRepository, feedParser)
    val viewModel = rememberFeedViewModel(navController, feedsUseCase, "")

    AppTheme {
        FeedScreen(viewModel = viewModel)
    }
}