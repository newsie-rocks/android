package rocks.newsie.app.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import rocks.newsie.app.domain.Feed

@Dao
interface FeedDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(feed: Feed)

    @Update
    suspend fun update(feed: Feed)

    @Delete
    suspend fun delete(feed: Feed)

    @Query("SELECT * from feeds WHERE id = :id")
    fun getFeed(id: String): Flow<Feed>

    @Query("SELECT * from feeds ORDER BY url ASC")
    fun getAllFeeds(): Flow<List<Feed>>
}

class FeedRepository(
    context: Context,
) {
    private val db = AppDatabase.getInstance(context)
    private val feedDao: FeedDao = db.feedDao()

    suspend fun insert(feed: Feed) = feedDao.insert(feed)
    suspend fun update(feed: Feed) = feedDao.update(feed)
    suspend fun delete(feed: Feed) = feedDao.delete(feed)
    fun getFeed(id: String): Flow<Feed> = feedDao.getFeed(id)
    fun getAllFeeds(): Flow<List<Feed>> = feedDao.getAllFeeds()
}