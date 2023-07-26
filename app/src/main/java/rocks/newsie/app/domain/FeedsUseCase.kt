package rocks.newsie.app.domain

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.first
import rocks.newsie.app.data.FeedParser
import rocks.newsie.app.data.FeedRepository
import java.util.UUID

class FeedsUseCase(
    private val feedRepository: FeedRepository,
    private val feedParser: FeedParser,
) {
    fun getFeeds() = feedRepository.getAllFeeds()
    fun getFeed(feedId: String) = feedRepository.getFeed(feedId)
    fun getFeedArticles(feedId: String) = feedRepository.getArticlesForFeed(feedId)

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun addNewFeed(url: String, name: String?) {
        // NB: validate the feed here
        val foundFeed = feedRepository.getFeedWithUrl(url).first()
        if (foundFeed != null) {
            throw Exception("Feed already added")
        }

        val feed = feedParser.parse(url)
        Log.d("FeedsUseCase", "addNewFeed: feed=$feed")
        feed.id = UUID.randomUUID().toString()
        feed.name = name
        feedRepository.insertFeed(feed)

        // NB: add articles
        val articles = feed.articles.map {
            it.id = UUID.randomUUID().toString()
            it.feedId = feed.id
            it
        }
        for (article in articles) {
            feedRepository.insertArticle(article)
        }
    }
}

@Composable
fun rememberFeedsUseCase(
    feedRepository: FeedRepository,
    feedParser: FeedParser,
): FeedsUseCase {
    return remember {
        FeedsUseCase(
            feedRepository = feedRepository,
            feedParser = feedParser,
        )
    }
}

