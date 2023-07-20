package rocks.newsie.app.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "feeds")
data class Feed(
    @PrimaryKey
    val id: String,
    val url: String,
    val name: String,
)

@Dao
interface FeedDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(feed: Feed)

    @Update
    suspend fun update(feed: Feed)

    @Delete
    suspend fun delete(feed: Feed)

    @Query("SELECT * from feeds WHERE id = :id")
    fun getFeed(id: Int): Flow<Feed>

    @Query("SELECT * from feeds ORDER BY url ASC")
    fun getAllFeeds(): Flow<List<Feed>>
}

@Database(entities = [Feed::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun feedDao(): FeedDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "item_database",
                ).build().also {
                    Instance = it
                }
            }
        }
    }
}

class DataRoom(
    context: Context,
) {
    private val db = AppDatabase.getInstance(context)
    private val feedDao: FeedDao = db.feedDao()

    suspend fun insert(feed: Feed) = feedDao.insert(feed)
    suspend fun update(feed: Feed) = feedDao.update(feed)
    suspend fun delete(feed: Feed) = feedDao.delete(feed)
    fun getFeed(id: Int): Flow<Feed> = feedDao.getFeed(id)
    fun getAllFeeds(): Flow<List<Feed>> = feedDao.getAllFeeds()
}