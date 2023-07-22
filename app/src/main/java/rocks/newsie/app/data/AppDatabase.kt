package rocks.newsie.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import rocks.newsie.app.domain.Feed

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
                    "app_data",
                ).build().also {
                    Instance = it
                }
            }
        }
    }
}