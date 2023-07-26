package rocks.newsie.app.domain

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.first
import rocks.newsie.app.data.FeedParser
import rocks.newsie.app.data.FeedRepository
import rocks.newsie.app.data.extractArticles
import rocks.newsie.app.data.extractFeed
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
        // NB: check if feed already exists
        // FIXME: this is not working
        val feedInDb = feedRepository.getFeedWithUrl(url).first()
        if (feedInDb != null) {
            throw Exception("Feed already added")
        }

        // read and parse the XML feed
        val channel = feedParser.parse(url)
//        Log.d("FeedsUseCase", "addNewFeed: channel=$channel")
        val feed = channel.extractFeed()
        feed.id = UUID.randomUUID().toString()
        feed.name = name
        feedRepository.insertFeed(feed)

        // NB: add articles
        val articles = channel.extractArticles().map {
            it.id = UUID.randomUUID().toString()
            it.feedId = feed.id
            it
        }
        feedRepository.insertArticles(*articles.toTypedArray())
    }

    suspend fun deleteFeed(feed: Feed) {
        feedRepository.deleteAllArticlesForFeed(feed.id)
        feedRepository.deleteFeed(feed)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun loadArticles(feed: Feed): List<Article> {
        // check existing articles in DB
        val articlesInDb = feedRepository.getArticlesForFeed(feed.id).first()
        val articlesInDbIds = articlesInDb.map { it.id }

        // load the feed and check new articles
        val channel = feedParser.parse(feed.link)
        val newArticles = channel.extractArticles().filter {
            !articlesInDbIds.contains(it.id)
        }.map {
            it.id = UUID.randomUUID().toString()
            it.feedId = feed.id
            it
        }
        feedRepository.insertArticles(*newArticles.toTypedArray())

        return articlesInDb + newArticles
    }

    fun getArticle(articleId: String) = feedRepository.getArticle(articleId)
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

