package rocks.newsie.app.data

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
    suspend fun parse(url: String): Feed {
        try {
            val channel = parser.getChannel(url)
            return Feed(
                id = "",
                name = "",
                link = channel.link ?: url,
                title = channel.title,
                description = channel.description,
                image = channel.image?.let {
                    Image(
                        title = it.title,
                        url = it.url,
                        link = it.link,
                    )
                },
                articles = channel.articles.map {
                    Article(
                        id = "",
                        feedId = "",
                        link = it.link ?: "",
                        guid = it.guid,
                        title = it.title,
                        author = it.author,
                        description = it.description,
                        content = it.content,
                        pubDate = try {
                            ZonedDateTime.parse(it.pubDate)
                        } catch (e: Exception) {
                            ZonedDateTime.now()
                        },
                    )
                }
            )
        } catch (e: Exception) {
            throw e
        }
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
