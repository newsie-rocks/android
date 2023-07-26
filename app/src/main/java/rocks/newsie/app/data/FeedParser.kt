package rocks.newsie.app.data

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.prof.rssparser.Channel
import com.prof.rssparser.Parser
import rocks.newsie.app.domain.Article
import rocks.newsie.app.domain.Feed
import rocks.newsie.app.domain.Image
import java.nio.charset.Charset
import java.time.ZonedDateTime

class FeedParser(
    val context: Context,
) {
    private val parser = Parser.Builder()
        .context(context = context)
        .charset(Charset.forName("ISO-8859-7"))
        .cacheExpirationMillis(0) // one day
        .build()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun parse(url: String): Channel {
        return parser.getChannel(url)
    }
}

fun Channel.extractFeed(): Feed {
    return Feed(
        id = "",
        name = "",
        link = this.link ?: "",
        title = this.title,
        description = this.description,
        image = this.image?.let {
            Image(
                title = it.title,
                url = it.url,
                link = it.link,
            )
        },
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun Channel.extractArticles(): List<Article> {
    return this.articles.map {
        val pubDate = try {
            ZonedDateTime.parse(it.pubDate)
        } catch (e: Exception) {
            ZonedDateTime.now()
        }

        Article(
            id = "",
            feedId = "",
            link = it.link ?: "",
            guid = it.guid,
            title = it.title,
            author = it.author,
            description = it.description,
            content = it.content,
            pubDate = pubDate,
        )
    }
}

@Composable
fun rememberFeedParser(
    context: Context,
): FeedParser {
    return remember {
        FeedParser(context = context)
    }
}
