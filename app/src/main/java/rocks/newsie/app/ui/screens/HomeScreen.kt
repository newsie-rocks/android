package rocks.newsie.app.ui.screens

import android.os.Parcelable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import rocks.newsie.app.data.FeedRepository
import rocks.newsie.app.domain.Feed
import rocks.newsie.app.domain.FeedsUseCase
import rocks.newsie.app.ui.partials.AddFeed
import rocks.newsie.app.ui.partials.Logo
import rocks.newsie.app.ui.theme.AppTheme

fun NavGraphBuilder.homeScreen(
    navController: NavController,
    feedsUseCase: FeedsUseCase,
) {
    composable("home") {
        val viewModel = rememberHomeScreenViewModel(navController, feedsUseCase)
        HomeScreen(viewModel)
    }
}

fun NavController.navigateToHome() {
    this.navigate("home")
}

class HomeScreenViewModel(
    private val navController: NavController,
    private val feedsUseCase: FeedsUseCase,
    addFeedSheetIsOpened: Boolean = false,
) {
    var addFeedSheetIsOpened by mutableStateOf(addFeedSheetIsOpened)
        private set

    val feeds = feedsUseCase.getFeeds()

    fun openAddFeed() {
        addFeedSheetIsOpened = true
    }

    fun closeAddFeed() {
        addFeedSheetIsOpened = false
    }

    suspend fun addNewFeed(feed: Feed) {
        feedsUseCase.addFeed(feed)
        addFeedSheetIsOpened = false
    }

    fun onGoToSettings() {
        navController.navigateToSettings()
    }

    fun goToFeedScreen(feedId: String) {
        navController.navigateToFeed(feedId)
    }
}

@Parcelize
data class HomeScreenViewModelHolder(val addFeedIsOpened: Boolean) : Parcelable

@Composable
fun rememberHomeScreenViewModel(
    navController: NavController,
    feedsUseCase: FeedsUseCase,
): HomeScreenViewModel {
    return rememberSaveable(saver = Saver(
        save = {
            HomeScreenViewModelHolder(addFeedIsOpened = it.addFeedSheetIsOpened)
        },
        restore = {
            HomeScreenViewModel(
                navController = navController,
                feedsUseCase = feedsUseCase,
                addFeedSheetIsOpened = it.addFeedIsOpened
            )
        }
    )) {
        HomeScreenViewModel(
            navController,
            feedsUseCase
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
) {
    val coroutineScope = rememberCoroutineScope()
    val feeds by viewModel.feeds.collectAsState(initial = listOf())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Logo()
                },
                actions = {
                    IconButton(onClick = { viewModel.openAddFeed() }) {
                        Icon(Icons.Rounded.Add, "Add a feed")
                    }
                    IconButton(onClick = { viewModel.onGoToSettings() }) {
                        Icon(Icons.Rounded.Settings, "Open the menu")
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                feeds.forEach { feed ->
                    FeedRow(
                        feed,
                        onClickRow = { feedId ->
                            viewModel.goToFeedScreen(feedId)
                        },
                    )
                }
            }

            // new feed sheet
            AddFeedBottomSheet(
                isOpened = viewModel.addFeedSheetIsOpened,
                onDismiss = { viewModel.closeAddFeed() },
                onSubmit = {
                    coroutineScope.launch {
                        viewModel.addNewFeed(it)
                    }
                }
            )
        }
    )
}

@Composable
fun FeedRow(feed: Feed, onClickRow: (feedId: String) -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClickRow(feed.id)
            },
        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(feed.name)
            Icon(Icons.Rounded.KeyboardArrowRight, "Go to feed")
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFeedBottomSheet(
    isOpened: Boolean = false,
    onDismiss: () -> Unit = {},
    onSubmit: (Feed) -> Unit = {},
) {
    val state = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    if (isOpened) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = state,
            windowInsets = WindowInsets.safeContent,
        ) {
            AddFeed(
                onSubmit = onSubmit,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 32.dp, top = 0.dp)
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
fun HomeScreenPreview() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val feedRepository = FeedRepository(context)
    val feedsUseCase = FeedsUseCase(feedRepository)
    val viewModel = rememberHomeScreenViewModel(navController, feedsUseCase)

    AppTheme {
        HomeScreen(viewModel)
    }
}