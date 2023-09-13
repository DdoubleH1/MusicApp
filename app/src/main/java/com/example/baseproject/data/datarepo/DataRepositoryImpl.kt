package com.example.baseproject.data.datarepo

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toUri
import com.example.baseproject.R
import com.example.baseproject.data.model.PlaylistSongItem
import com.example.baseproject.utils.AudioUtils
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths


class DataRepositoryImpl : DataRepository {

    override fun getSong(context: Context): List<PlaylistSongItem> {
//        val sArtworkUri = Uri.parse("content://media/external/audio/albumart")
        val musicList = mutableListOf<PlaylistSongItem>()
        val musicResolver: ContentResolver = context.contentResolver
        val musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val musicCursor = musicResolver.query(musicUri, null, null, null, null)
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            val titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val albumID = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
            val artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val songLink = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            //add songs to list
            do {
                val thisId = musicCursor.getInt(idColumn)
                val thisTitle = musicCursor.getString(titleColumn)
                val thisArtist = musicCursor.getString(artistColumn)
                val thisSongLink = Uri.parse(musicCursor.getString(songLink))
                val some = musicCursor.getLong(albumID)
                val drawableResourceId = R.drawable.ic_music
                var uri: Uri = Uri.Builder()
                    .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                    .authority(context.resources.getResourcePackageName(drawableResourceId))
                    .appendPath(context.resources.getResourceTypeName(drawableResourceId))
                    .appendPath(context.resources.getResourceEntryName(drawableResourceId))
                    .build()


                val audioUri = Uri.fromFile(File(thisSongLink.toString()))
                val audioUtils = AudioUtils()
                val trackArtUri = audioUtils.getTrackArtUri(context, audioUri)
                if (trackArtUri != null) {
                    uri = trackArtUri
                }

                if (thisSongLink.toString().endsWith(".mp3"))
                    musicList.add(
                        PlaylistSongItem(
                            thisId,
                            uri.toString(),
                            thisTitle,
                            thisArtist,
                            thisSongLink.toString()
                        )
                    )
            } while (musicCursor.moveToNext())
            assert(musicCursor != null)
            musicCursor.close()
        }
        return musicList
    }
}
