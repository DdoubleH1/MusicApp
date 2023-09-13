package com.example.baseproject.data

import android.icu.text.CaseMap.Title
import android.media.Image
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.baseproject.data.model.LibraryItem
import com.example.baseproject.data.model.PlaylistSongItem
import com.example.baseproject.data.relation.PlaylistWithSongs
import com.example.baseproject.data.relation.SongPlaylistCrossRef
import com.example.baseproject.data.relation.SongWithPlaylists
import com.example.setting.ui.home.HomeResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow

class MusicRepository(private val musicDao: MusicDao) {
    suspend fun addSong(item: PlaylistSongItem) {
        musicDao.addSong(item)
    }

    fun getAllSong() = flow {
        coroutineScope {
            val listSong = async { musicDao.listAllSong() }
            emit(listSong.await())
        }
    }

    suspend fun getAllPlaylist(): List<LibraryItem> {
        return musicDao.listAllPlaylist()
    }

    suspend fun addPlaylist(item: LibraryItem) {
        musicDao.addPlaylist(item)
    }

    suspend fun getSongsOfPlaylist(id: String): PlaylistWithSongs {
        return musicDao.getSongsofPlaylist(id)
    }

    fun getPlaylistsOfSong(id: Int) = flow {
        coroutineScope {
            val playlists = async { musicDao.getPlaylistsOfSong(id) }
            emit(playlists.await())
        }
    }

    suspend fun updatePlaylist(id: String, title: String) {
        musicDao.updatePlaylistTitle(id, title)
    }

    suspend fun updataImage(id: String, image: String?) {
        musicDao.updatePlaylistImage(id, image)
    }

    suspend fun getPLaylistSize(id: String): Int {
        return musicDao.getSongsofPlaylist(id).songs.size
    }

    suspend fun addSongPlaylistCrossRef(crossRef: SongPlaylistCrossRef) {
        musicDao.addSongPlaylistCrossRef(crossRef)
    }

    suspend fun deleteSongPlaylistCrossRef(crossRef: SongPlaylistCrossRef) {
        musicDao.deleteSongPlaylistCrossRef(crossRef)
    }

    suspend fun deleteData() {
        musicDao.deleteData()
    }

    suspend fun getAllCrossRef(): List<SongPlaylistCrossRef> {
        return musicDao.getAllCrossRef()
    }

    suspend fun getPLaylist(id: String): LibraryItem {
        return musicDao.getPlaylist(id)
    }

    suspend fun deletePlaylists() {
        musicDao.deletePlaylist()
    }
}