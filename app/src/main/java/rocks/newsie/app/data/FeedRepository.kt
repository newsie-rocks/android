package rocks.newsie.app.data

data class Feed(
    val id: String,
    val url: String,
    val name: String,
)

data class Article(
    val id: String,
    val url: String,
)

class FeedRepository {
    /**
     *
     */
    private val feeds: List<Feed> = listOf()

    /**
     * Exposes the account feeds
     */
    fun feeds(): List<Feed> {
        return feeds
    }

    /**
     * Loads the feeds for the account
     */
    fun load() {
        TODO("load the feeds")
    }
}