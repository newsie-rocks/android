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
import kotlinx.coroutines.flow.map
import kotlinx.parcelize.Parcelize
import rocks.newsie.app.data.FeedRepository
import rocks.newsie.app.domain.Article
import rocks.newsie.app.domain.Feed
import rocks.newsie.app.domain.FeedsUseCase
import rocks.newsie.app.ui.theme.AppTheme


fun NavGraphBuilder.feedScreen(
    navController: NavController,
    feedsUseCase: FeedsUseCase,
) {
    composable("feeds/{feedId}") {
        val feedId = it.arguments?.getString("feedId").toString()
        val viewModel = rememberFeedScreenViewModel(
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

class FeedScreenViewModel(
    private val navController: NavController,
    private val feedsUseCase: FeedsUseCase,
    val feedId: String,
) {
    val feed = feedsUseCase.getFeed(feedId)
    val articles = feed.map { it.loadArticles() }

    fun onGoBack() {
        navController.popBackStack()
    }

    fun goToArticle(url: String) {
        navController.navigateToArticle(url)
    }
}

@Parcelize
data class FeedScreenViewModelHolder(val feedId: String) : Parcelable

@Composable
fun rememberFeedScreenViewModel(
    navController: NavController,
    feedsUseCase: FeedsUseCase,
    feedId: String,
): FeedScreenViewModel {
    return rememberSaveable(saver = Saver(
        save = {
            FeedScreenViewModelHolder(feedId = it.feedId)
        },
        restore = {
            FeedScreenViewModel(
                navController = navController,
                feedsUseCase = feedsUseCase,
                feedId = it.feedId
            )
        }
    )) {
        FeedScreenViewModel(
            navController,
            feedsUseCase,
            feedId,
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    viewModel: FeedScreenViewModel,
) {
    val feed by viewModel.feed.collectAsState(
        initial = Feed(
            id = "",
            url = "",
            name = "",
        )
    )
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
                    Text("Feed ${feed.name}")
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
        Text("Article ${article.url}")
        Icon(Icons.Rounded.ArrowForward, "Go to article")
    }
}

@Preview(showBackground = false)
@Composable
fun FeedScreenPreview() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val feedRepository = FeedRepository(context)
    val feedsUseCase = FeedsUseCase(feedRepository)
    val feedId = ""
    val viewModel = rememberFeedScreenViewModel(navController, feedsUseCase, feedId)

    AppTheme {
        FeedScreen(viewModel = viewModel)
    }
}