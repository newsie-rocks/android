package rocks.newsie.app.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feeds")
data class Feed(
    @PrimaryKey
    val id: String,
    val url: String,
    val name: String,
) {
    fun loadArticles(): List<Article> {
        return listOf(
            Article(url = "https://www.nasa.gov/rss/dyn/breaking_news.rss"),
        )
    }
}