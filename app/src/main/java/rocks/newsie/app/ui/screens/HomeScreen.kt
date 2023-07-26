package rocks.newsie.app.ui.screens

import android.os.Build
import android.os.Parcelable
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import rocks.newsie.app.data.FeedParser
import rocks.newsie.app.data.FeedRepository
import rocks.newsie.app.domain.Feed
import rocks.newsie.app.domain.FeedsUseCase
import rocks.newsie.app.ui.partials.AddFeed
import rocks.newsie.app.ui.partials.Logo
import rocks.newsie.app.ui.theme.AppTheme

@RequiresApi(Build.VERSION_CODES.O)
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

class HomeViewModel(
    private val navController: NavController,
    private val feedsUseCase: FeedsUseCase,
) {
    var isAddFeedSheetOpened by mutableStateOf(false)
        private set

    var isAddFeedSheetInProgress by mutableStateOf(false)
        private set

    var errorAddFeed by mutableStateOf<String?>(null)
        private set

    val feeds = feedsUseCase.getFeeds()

    fun openAddFeedSheet() {
        isAddFeedSheetOpened = true
    }

    fun closeAddFeedSheet() {
        isAddFeedSheetOpened = false
        errorAddFeed = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun addNewFeed(url: String, name: String?) {
        try {
            isAddFeedSheetInProgress = true
            feedsUseCase.addNewFeed(url, name)
            isAddFeedSheetOpened = false
        } catch (e: Exception) {
            // NB: feed is invalid
            Log.e("ERR", e.toString())
            errorAddFeed = e.message
        } finally {
            isAddFeedSheetInProgress = false
        }
    }

    suspend fun deleteFeed(feed: Feed) {
        feedsUseCase.deleteFeed(feed)
    }

    fun onGoToSettings() {
        navController.navigateToSettings()
    }

    fun onGoToFeed(feed: Feed) {
        navController.navigateToFeed(feed.id)
    }
}

@Parcelize
data class HomeViewModelSaver(val dummy: Int) : Parcelable

@Composable
fun rememberHomeScreenViewModel(
    navController: NavController,
    feedsUseCase: FeedsUseCase,
): HomeViewModel {
    return rememberSaveable(saver = Saver(
        save = {
            HomeViewModelSaver(dummy = 0)
        },
        restore = {
            HomeViewModel(
                navController = navController,
                feedsUseCase = feedsUseCase,
            )
        }
    )) {
        HomeViewModel(
            navController,
            feedsUseCase
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
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
                    IconButton(onClick = { viewModel.openAddFeedSheet() }) {
                        Icon(Icons.Rounded.Add, "Add a feed")
                    }
                    IconButton(onClick = { viewModel.onGoToSettings() }) {
                        Icon(Icons.Rounded.Settings, "Open the menu")
                    }
                }
            )
        },
        content = { innerPadding ->
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(feeds) { feed ->
                    FeedRow(
                        feed,
                        onClickRow = { thisFeed ->
                            viewModel.onGoToFeed(thisFeed)
                        },
                        onDelete = { thisFeed ->
                            coroutineScope.launch(Dispatchers.IO) {
                                viewModel.deleteFeed(thisFeed)
                            }
                        }
                    )
                    Divider()
                }
            }

            // new feed sheet
            AddFeedBottomSheet(
                isOpened = viewModel.isAddFeedSheetOpened,
                onDismiss = { viewModel.closeAddFeedSheet() },
                onSubmit = { url, name ->
                    coroutineScope.launch(Dispatchers.IO) {
                        viewModel.addNewFeed(url, name)
                    }
                },
                error = viewModel.errorAddFeed,
                isInProgress = viewModel.isAddFeedSheetInProgress,
            )
        }
    )
}

@Composable
private fun FeedRow(
    feed: Feed,
    onClickRow: (feed: Feed) -> Unit = {},
    onDelete: (feed: Feed) -> Unit = {},
) {
    var isMenuOpened by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickRow(feed) }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .width(52.dp)
                .padding(end = 20.dp)
        ) {
            if (!feed.image?.url.isNullOrEmpty()) {
                AsyncImage(
                    model = feed.image?.url,
                    contentDescription = "Feed logo",
                )
            }
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text("${feed.name ?: feed.title}", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text("${feed.description}", fontSize = 12.sp, lineHeight = 20.sp)
        }
        IconButton(onClick = { isMenuOpened = true }) {
            Icon(Icons.Rounded.MoreVert, contentDescription = "Open the feed menu")
            DropdownMenu(
                expanded = isMenuOpened,
                onDismissRequest = { isMenuOpened = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = { onDelete(feed) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddFeedBottomSheet(
    isOpened: Boolean = false,
    onDismiss: () -> Unit,
    onSubmit: (url: String, name: String?) -> Unit,
    error: String? = null,
    isInProgress: Boolean = false,
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
                error = error,
                isInProgress = isInProgress,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 40.dp, top = 0.dp)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = false)
@Composable
fun HomeScreenPreview() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val feedRepository = FeedRepository(context)
    val feedParser = FeedParser(context)
    val feedsUseCase = FeedsUseCase(feedRepository, feedParser)
    val viewModel = rememberHomeScreenViewModel(navController, feedsUseCase)

    AppTheme {
        HomeScreen(viewModel)
    }
}