package rocks.newsie.app.data

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.Flow
import rocks.newsie.app.domain.Article
import rocks.newsie.app.domain.Feed


class FeedRepository(
    context: Context,
) {
    private val db = AppDatabase.getInstance(context)
    private val feedDao: FeedDao = db.feedDao()
    private val articleDao: ArticleDao = db.articleDao()

    suspend fun insertFeed(feed: Feed) = feedDao.insert(feed)
    suspend fun updateFeed(feed: Feed) = feedDao.update(feed)
    suspend fun deleteFeed(feed: Feed) = feedDao.delete(feed)
    fun getFeed(id: String): Flow<Feed?> = feedDao.get(id)
    fun getAllFeeds(): Flow<List<Feed>> = feedDao.getAll()
    fun getFeedWithUrl(url: String): Flow<Feed?> = feedDao.getWithUrl(url)

    suspend fun insertArticle(article: Article) = articleDao.insert(article)
    suspend fun updateArticle(article: Article) = articleDao.update(article)
    suspend fun deleteArticle(article: Article) = articleDao.delete(article)
    fun getArticle(id: String): Flow<Article?> = articleDao.get(id)
    fun getAllArticles(): Flow<List<Article>> = articleDao.getAll()
    fun getArticlesForFeed(feedId: String): Flow<List<Article>> = articleDao.getAllForFeed(feedId)
}

@Composable
fun rememberFeedRepository(
    context: Context,
): FeedRepository {
    return remember {
        FeedRepository(context = context)
    }
}