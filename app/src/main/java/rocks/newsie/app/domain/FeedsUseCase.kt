package rocks.newsie.app.domain

import rocks.newsie.app.data.FeedRepository
import java.util.UUID

class FeedsUseCase(
    private val feedRepository: FeedRepository,
) {
    fun getFeeds() = feedRepository.getAllFeeds()

    fun getFeed(feedId: String) = feedRepository.getFeed(feedId)

    suspend fun addFeed(feed: Feed) {
        val feedWithId = feed.copy(id = UUID.randomUUID().toString())
        feedRepository.insert(feedWithId)
    }
}

