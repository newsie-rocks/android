package rocks.newsie.app.domain

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey
    var id: String,
    var feedId: String,
    var guid: String? = null,
    var link: String,
    var title: String? = null,
    var author: String? = null,
    var description: String? = null,
    var pubDate: ZonedDateTime? = null,
    var content: String? = null,
    @Embedded(prefix = "image") var image: Image? = null,
//    @Embedded var categories: List<String>?,
)