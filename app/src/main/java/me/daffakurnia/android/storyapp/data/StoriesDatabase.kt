package me.daffakurnia.android.storyapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.daffakurnia.android.storyapp.response.StoriesResponseItem

@Database(
    entities = [StoriesResponseItem::class],
    version = 1,
    exportSchema = false
)
abstract class StoriesDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: StoriesDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): StoriesDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoriesDatabase::class.java, "story_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}