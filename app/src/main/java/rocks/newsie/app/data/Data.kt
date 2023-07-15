package rocks.newsie.app.data

/**
 * A feed
 */
data class Feed(
    val url: String
)

/**
 * A collection of feeds
 */
data class FeedCollection(
    val id: String,
    val name: String,
    val feeds: List<Feed>,
)