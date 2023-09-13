package com.example.baseproject.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.baseproject.data.model.LibraryItem
import com.example.baseproject.data.model.PlaylistSongItem
import com.example.baseproject.data.relation.SongPlaylistCrossRef

@Database(
    entities = [
        PlaylistSongItem::class,
        LibraryItem::class,
        SongPlaylistCrossRef::class
    ], version = 1
)
abstract class MusicDatabase : RoomDatabase() {
    abstract fun musicDao(): MusicDao

    companion object {
        @Volatile
        private var INSTANCE: MusicDatabase? = null
        fun getDatabase(context: Context): MusicDatabase {
            val temp = INSTANCE
            if (temp != null)
                return temp
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MusicDatabase::class.java,
                    "music_data"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}