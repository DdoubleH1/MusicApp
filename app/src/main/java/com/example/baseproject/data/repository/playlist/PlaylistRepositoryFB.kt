package com.example.baseproject.data.repository.playlist

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.baseproject.data.model.LibraryItem
import com.example.baseproject.data.relation.SongPlaylistCrossRef
import com.example.baseproject.utils.Response

interface PlaylistRepositoryFB {
    fun getPlaylist(): MutableLiveData<Response<List<LibraryItem>>>
    fun getCrossRef(): MutableLiveData<Response<List<SongPlaylistCrossRef>>>
    suspend fun updateCrossRef(list: List<SongPlaylistCrossRef>): Response<Boolean>
    suspend fun addPlaylist(item: LibraryItem): Response<Boolean>
    suspend fun updatePlaylist(id: String, title: String, image: String?): Response<Boolean>
    suspend fun getSongOfPlaylist(id: String): Response<List<SongPlaylistCrossRef>>

}