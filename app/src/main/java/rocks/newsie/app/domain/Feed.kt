package rocks.newsie.app.domain

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "feeds")
data class Feed constructor(
    @PrimaryKey
    var id: String,
    var link: String,
    var name: String? = null,
    var title: String? = null,
    var description: String? = null,
    @Embedded(prefix = "image") var image: Image? = null,
    @Ignore var articles: List<Article> = emptyList(),
) {
    constructor() : this(
        id = "",
        link = "",
    )
}