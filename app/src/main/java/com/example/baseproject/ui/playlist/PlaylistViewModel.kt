package com.example.baseproject.ui.playlist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.baseproject.data.MusicDatabase
import com.example.baseproject.data.MusicRepository
import com.example.baseproject.data.model.LibraryItem
import com.example.baseproject.data.model.PlaylistSongItem
import com.example.baseproject.data.model.WaitList
import com.example.baseproject.data.relation.SongPlaylistCrossRef
import com.example.baseproject.data.repository.playlist.PlaylistRepositoryFB
import com.example.baseproject.service.MusicService
import com.example.core.base.BaseViewModel
import com.example.core.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    application: Application,
    private val playlistRepositoryFB: PlaylistRepositoryFB,
) : BaseViewModel() {
    private val repository: MusicRepository
    var firstInit = MutableLiveData<Boolean>()
    var musicService = MutableLiveData<MusicService>()
    var isShuffle = MutableLiveData<Boolean>()
    var btnState = MutableLiveData<Int>()
    var waitList = WaitList()

    init {
        val musicDao = MusicDatabase.getDatabase(application).musicDao()
        repository = MusicRepository(musicDao)
        firstInit.postValue(false)
        isShuffle.postValue(false)
        btnState.postValue(0)
    }

    fun prepare(position: Int) {
        waitList.waitList.clear()
        waitList.history.clear()
        waitList.history.addLast(songList.value!![position])
        for (i in songList.value!!)
            if (i !in waitList.history) {
                waitList.waitList.addLast(i)
            }
    }

    fun setFirstInit() {
        firstInit.value = true
    }

    val cPlaylist = MutableLiveData<LibraryItem>()

    fun getData(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            cPlaylist.postValue(repository.getPLaylist(id))
        }
    }
    // hiển thị danh sách các bài hát thuộc playlist có id là id

    private var _songList = MutableLiveData<List<PlaylistSongItem>>()
    var songList: LiveData<List<PlaylistSongItem>> = _songList

    fun getSong(id: String) {
        if (id != "-1") {
            viewModelScope.launch(Dispatchers.IO) {
                _songList.postValue(repository.getSongsOfPlaylist(id).songs)
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                _songList.postValue(repository.getAllSong().first())
            }
        }
    }

    fun addSongtoPlaylist(songId: Int, playlistId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addSongPlaylistCrossRef(SongPlaylistCrossRef(songId, playlistId))
        }
    }


// hiển thị các bài hát để thêm vào playlist hiện tại

    val addSongList = MutableLiveData<List<PlaylistSongItem>>()


    fun listAll() {
        repository.getAllSong()
            .flowOn(Dispatchers.IO)
            .onStart {
                isLoading.value = true
            }.onCompletion {
                isLoading.value = false
            }.onEach {

                addSongList.value = addDialogFilter(it)
            }.catch {
                messageError.value = it.message
            }.launchIn(viewModelScope)
    }

    private fun addDialogFilter(list: List<PlaylistSongItem>): List<PlaylistSongItem> {
        val data = mutableListOf<Int>()
        songList.value?.forEach {
            data.add(it.songId)
        }
        val result = mutableListOf<PlaylistSongItem>()
        list.forEach {
            if (!data.contains(it.songId)) {
                result.add(it)
            }
        }
        return result
    }

    // play fragment dialog
    val tPlaylistListId = SingleLiveEvent<List<String>>()

    fun getPlaylistOfSong(id: Int) {
        repository.getPlaylistsOfSong(id)
            .flowOn(Dispatchers.IO)
            .onStart {
                isLoading.value = true
            }.onCompletion {
                isLoading.value = false
            }.onEach {
                tPlaylistListId.value = filter(it.playlists)
            }.catch {
                messageError.value = it.message
            }.launchIn(viewModelScope)
    }

    private fun filter(list: List<LibraryItem>): List<String> {
        val dataId: MutableList<String> = mutableListOf()
        list.forEach {
            dataId.add(it.playlistId)
        }
        return dataId
    }

// custom fragment

    fun reset(newList: List<String>, oldList: List<String>, songId: Int) {
        newList.forEach {
            if (it !in oldList) {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.addSongPlaylistCrossRef(SongPlaylistCrossRef(songId, it))
                }

            }
        }

        oldList.forEach {
            if (it !in newList) {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.deleteSongPlaylistCrossRef(SongPlaylistCrossRef(songId, it))
                }
            }

        }
    }

// lấy dữ liệu về các playlist

    val playlists = SingleLiveEvent<List<LibraryItem>>()
    fun getAllPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.getAllPlaylist()
            playlists.postValue(data)
        }
    }

// edit playlist

    fun edit(list: List<Int>, playlistId: String, new_title: String, image: String?) {
        list.forEach {
            viewModelScope.launch(Dispatchers.IO) {
                repository.deleteSongPlaylistCrossRef(SongPlaylistCrossRef(it, playlistId))
            }
        }
        if (new_title != "") {
            viewModelScope.launch(Dispatchers.IO) {
                repository.updatePlaylist(playlistId, new_title)
            }
            viewModelScope.launch(Dispatchers.IO) {
                playlistRepositoryFB.updatePlaylist(playlistId, new_title, image)
            }
            updateTitle(new_title)
        }
        if (image != null) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.updataImage(playlistId, image)
            }
            updateImage(image)
        }
    }

    val title = MutableLiveData<String>()
    private fun updateTitle(nTitle: String) {
        title.postValue(nTitle)
    }

    val image = MutableLiveData<String?>()
    private fun updateImage(nImage: String?) {
        image.postValue(nImage)
    }

//


    // các chức năng khác
    private fun convert(str: String?): String {
        var str = str
        str = str!!.replace("[àáạảãâầấậẩẫăằắặẳẵ]".toRegex(), "a")
        str = str.replace("[èéẹẻẽêềếệểễ]".toRegex(), "e")
        str = str.replace("[ìíịỉĩ]".toRegex(), "i")
        str = str.replace("[òóọỏõôồốộổỗơờớợởỡ]".toRegex(), "o")
        str = str.replace("[ùúụủũưừứựửữ]".toRegex(), "u")
        str = str.replace("[ỳýỵỷỹ]".toRegex(), "y")
        str = str.replace("đ".toRegex(), "d")
        str = str.replace("[ÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴ]".toRegex(), "A")
        str = str.replace("[ÈÉẸẺẼÊỀẾỆỂỄ]".toRegex(), "E")
        str = str.replace("[ÌÍỊỈĨ]".toRegex(), "I")
        str = str.replace("[ÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠ]".toRegex(), "O")
        str = str.replace("[ÙÚỤỦŨƯỪỨỰỬỮ]".toRegex(), "U")
        str = str.replace("[ỲÝỴỶỸ]".toRegex(), "Y")
        str = str.replace("Đ".toRegex(), "D")
        return str
    }

    fun filter(newText: String?, list: List<PlaylistSongItem>): List<PlaylistSongItem> {
        val filterList = ArrayList<PlaylistSongItem>()
        if (newText != null) {
            list.forEach {
                if (convert(it.songTitle).lowercase(Locale.ROOT)
                        .contains(convert(newText).lowercase())
                ) {
                    filterList.add(it)
                }
            }
            return if (filterList.isEmpty()) {
                val emptyList = ArrayList<PlaylistSongItem>()
                emptyList
            } else {
                filterList
            }
        }
        return filterList
    }

}