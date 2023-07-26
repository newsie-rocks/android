package rocks.newsie.app.ui.screens

import android.os.Parcelable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.parcelize.Parcelize
import rocks.newsie.app.data.rememberFeedParser
import rocks.newsie.app.data.rememberFeedRepository
import rocks.newsie.app.domain.Article
import rocks.newsie.app.domain.FeedsUseCase
import rocks.newsie.app.domain.rememberFeedsUseCase
import rocks.newsie.app.ui.theme.AppTheme


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

    fun goToArticle(url: String) {
        navController.navigateToArticle(url)
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    viewModel: FeedViewModel,
) {
    val feed by viewModel.feed.collectAsState(initial = null)
    val articles by viewModel.articles.collectAsState(initial = listOf())

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { viewModel.onGoBack() }) {
                        Icon(Icons.Rounded.ArrowBack, "Go back")
                    }
                },
                title = {
                    Text("Feed ${feed?.name}")
                },
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                articles.forEach { article ->
                    ArticleRow(
                        article,
                        onGoToArticle = { viewModel.goToArticle("todo") },
                    )
                }
            }
        }
    )
}

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
    ) {
        Column {
            Text("title: ${article.title}")
            Text("link: ${article.link}")
            Text("author: ${article.author}")
            Text("image: ${article.image}")
        }
    }
}

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