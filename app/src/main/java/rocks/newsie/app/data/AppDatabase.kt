package rocks.newsie.app.data

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import rocks.newsie.app.domain.Article
import rocks.newsie.app.domain.Feed
import java.time.ZonedDateTime

@Database(
    entities = [Feed::class, Article::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DbConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun feedDao(): FeedDao
    abstract fun articleDao(): ArticleDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_data",
                ).build().also {
                    Instance = it
                }
            }
        }
    }
}

class DbConverters {
    @RequiresApi(Build.VERSION_CODES.O)
//    val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")
    
    @TypeConverter
    fun fromTimestamp(value: String?): ZonedDateTime? {
        return value?.let { ZonedDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: ZonedDateTime?): String? {
        return date?.toString()
    }
}

@Dao
interface FeedDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(feed: Feed)

    @Update
    suspend fun update(feed: Feed)

    @Delete
    suspend fun delete(feed: Feed)

    @Query("SELECT * from feeds WHERE id = :id")
    fun get(id: String): Flow<Feed?>

    @Query("SELECT * from feeds ORDER BY link ASC")
    fun getAll(): Flow<List<Feed>>

    @Query("SELECT * from feeds WHERE link = :url LIMIT 1")
    fun getWithUrl(url: String): Flow<Feed?>
}

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(article: Article)

    @Update
    suspend fun update(article: Article)

    @Delete
    suspend fun delete(article: Article)

    @Query("SELECT * from articles WHERE id = :id")
    fun get(id: String): Flow<Article?>

    @Query("SELECT * from articles ORDER BY link ASC")
    fun getAll(): Flow<List<Article>>

    @Query("SELECT * from articles WHERE feedId = :feedId")
    fun getAllForFeed(feedId: String): Flow<List<Article>>
}
