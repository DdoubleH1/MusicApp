package com.example.baseproject.ui.play

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Palette.Swatch
import com.example.baseproject.R
import com.example.baseproject.data.model.LibraryItem
import com.example.baseproject.data.model.PlaylistSongItem
import com.example.baseproject.databinding.FragmentPlayDialogBinding
import com.example.baseproject.service.MusicService
import com.example.baseproject.ui.playlist.PlaylistViewModel
import com.example.baseproject.ui.playlist.customplaylist.CustomPLaylistDialog
import com.example.baseproject.utils.MusicTimer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.InputStream
import java.util.Collections


class PlayFragmentDialog() : BottomSheetDialogFragment() {
    private lateinit var dialogBinding: FragmentPlayDialogBinding
    private lateinit var musicService: MusicService
    private var isServiceConnected: Boolean = false
    private lateinit var intent: Intent
    private lateinit var bundle: Bundle

    private var isPlaying: Boolean = false
    private var data: List<LibraryItem>? = null
    private val handler = Handler()
    private val musicTimer = MusicTimer()
    private val viewModel: PlayViewModel by activityViewModels()
    private val playlistViewModel: PlaylistViewModel by activityViewModels()
    fun getVM() = viewModel

    private val mServiceConnection: ServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(componentName: ComponentName?, iBinder: IBinder?) {
            Log.e("HoangDH", "Service Connected")
            val myBinder: MusicService.MyBinder = iBinder as MusicService.MyBinder
            musicService = myBinder.getMyService()
            isServiceConnected = true
            initSeekBar()
            musicService.songLiveData.observe(viewLifecycleOwner) {
                bindingPlayerView(it)
            }
            musicService.songIsPlaying.observe(viewLifecycleOwner) {
                isPlaying = it
                if (isPlaying) {
                    dialogBinding.btnPlay.setImageResource(R.drawable.ic_pause)
                } else {
                    dialogBinding.btnPlay.setImageResource(R.drawable.ic_green_play)
                }
            }
            setOnClick()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isServiceConnected = false
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        viewModel.switchDismissDialog(false)
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        playlistViewModel.getAllPlaylists()

        dialogBinding = FragmentPlayDialogBinding.inflate(inflater, container, false)
        val intent = Intent(context, MusicService::class.java)
        context?.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)


        dialogBinding.apply {
            btnPlay.setImageResource(R.drawable.ic_pause)
            btnDown.setOnClickListener {
                dialog!!.dismiss()
            }
        }
        playlistViewModel.isShuffle.observe(viewLifecycleOwner) { isShuffle ->
            when (isShuffle) {
                false -> {
                    dialogBinding.btnShu.setImageResource(R.drawable.ic_shuffle)
                }

                true -> {
                    dialogBinding.btnShu.setImageResource(R.drawable.ic_clicked_shuffle)
                }
            }
        }

        playlistViewModel.btnState.observe(viewLifecycleOwner) { state ->
            when (state) {
                0 -> {
                    dialogBinding.btnRepeat.setImageResource(R.drawable.ic_repeat)
                }

                1 -> {
                    dialogBinding.btnRepeat.setImageResource(R.drawable.ic_clicked_repeat)
                }

                2 -> {
                    dialogBinding.btnRepeat.setImageResource(R.drawable.ic_repeat_one)
                }
            }
        }

        return dialogBinding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.switchDismissDialog(true)
        viewModel.dimissDialog.observe(viewLifecycleOwner) {

            Log.e("HoangDH", "onDismiss: ${viewModel.dimissDialog.value}")


        }
        playlistViewModel.getAllPlaylists()
    }

    private fun setOnClick() {

        dialogBinding.apply {

            btnRepeat.setOnClickListener {
                playlistViewModel.btnState.postValue((playlistViewModel.btnState.value!! + 1) % 3)
            }

            btnPlay.setOnClickListener {
                if (!isPlaying) {
                    playSound()
                    dialogBinding.btnPlay.setImageResource(R.drawable.ic_pause)
                } else {
                    pauseSound()
                    dialogBinding.btnPlay.setImageResource(R.drawable.ic_green_play)
                }
            }

            btnNext.setOnClickListener {
                disableButtonForOneSecond(dialogBinding.btnNext)
                if (playlistViewModel.isShuffle.value!!) {
                    Log.e("HoangDH", "shuffle")
                    when (musicService.songPosition < musicService.shuffleSongList.size - 1) {
                        true -> musicService.songPosition++
                        false -> if (playlistViewModel.btnState.value == 0) musicService.songPosition =
                            musicService.shuffleSongList.size - 1
                        else
                            musicService.songPosition = 0
                    }
                    prepareBundle(musicService.shuffleSongList[musicService.songPosition])
                    bindingPlayerView(musicService.shuffleSongList[musicService.songPosition])
                    startMusicService()
                } else {
                    Log.e("HoangDH", "not shuffle")
                    when (musicService.songPosition < musicService.songList.size - 1) {
                        true -> musicService.songPosition++
                        false -> if (playlistViewModel.btnState.value == 0) musicService.songPosition =
                            musicService.shuffleSongList.size - 1
                        else
                            musicService.songPosition = 0
                    }
                    prepareBundle(musicService.songList[musicService.songPosition])
                    bindingPlayerView(musicService.songList[musicService.songPosition])
                    startMusicService()
                }
            }

            btnPre.setOnClickListener {
                disableButtonForOneSecond(dialogBinding.btnPre)
                if (playlistViewModel.isShuffle.value!!) {
                    when (musicService.songPosition > 0) {
                        true -> musicService.songPosition--
                        false -> if (playlistViewModel.btnState.value == 0) musicService.songPosition =
                            0
                        else
                            musicService.songPosition = musicService.shuffleSongList.size - 1
                    }
                    prepareBundle(musicService.shuffleSongList[musicService.songPosition])
                    bindingPlayerView(musicService.shuffleSongList[musicService.songPosition])
                    startMusicService()
                } else {
                    when (musicService.songPosition > 0) {
                        true -> musicService.songPosition--
                        false -> if (playlistViewModel.btnState.value == 0) musicService.songPosition =
                            0
                        else
                            musicService.songPosition = musicService.songList.size - 1
                    }
                    prepareBundle(musicService.songList[musicService.songPosition])
                    bindingPlayerView(musicService.songList[musicService.songPosition])
                    startMusicService()
                }
            }

            btnFav.setOnClickListener {
                playlistViewModel.getPlaylistOfSong(musicService.songList[musicService.songPosition].songId)

                playlistViewModel.playlists.observe(viewLifecycleOwner) {
                    data = it
                }

                data?.let {
                    CustomPLaylistDialog(
                        data!!.toMutableList(),
                        musicService.songList[musicService.songPosition].songId
                    ).show(
                        childFragmentManager,
                        "custom playlist"
                    )
                }
            }

            dialogBinding.btnDown.setOnClickListener {
                this@PlayFragmentDialog.dismiss()
            }

            btnShu.setOnClickListener {
                playlistViewModel.isShuffle.postValue(
                    when (playlistViewModel.isShuffle.value!!) {
                        true -> {
                            dialogBinding.btnShu.setImageResource(R.drawable.ic_shuffle)
                            musicService.shuffleSong(false)
                            false
                        }

                        false -> {
                            dialogBinding.btnShu.setImageResource(R.drawable.ic_clicked_shuffle)
                            musicService.shuffleSong(true)
                            true
                        }
                    }
                )
            }
        }

    }

    private fun playSound() {
        musicService.startMusic()
    }

    private fun pauseSound() {
        musicService.pauseMusic()
    }

    @SuppressLint("SetTextI18n")
    private fun bindingPlayerView(song: PlaylistSongItem?) {
        dialogBinding.songImage.setImageURI(song?.songImage!!.toUri())
        dialogBinding.songTitle.text = "${song.songTitle}"
        dialogBinding.songArtis.text = "${song.artists}"
        val `is`: InputStream? =
            requireActivity().contentResolver.openInputStream(song.songImage!!.toUri())
        val bitmap = BitmapFactory.decodeStream(`is`)
        `is`?.close()
        dialogBinding.playBg.background = getDominantColor(bitmap)
        playlistViewModel.btnState.observe(viewLifecycleOwner) {
            when (it) {
                0 -> {
                    Log.d("trndwcs", "none")
                    musicService.repeatSong(false)
                    musicService.repeatPlaylist(false)
                }

                1 -> {
                    Log.d("trndwcs", "repeat playlist")
                    musicService.repeatSong(false)
                    musicService.repeatPlaylist(true)
                }

                2 -> {
                    Log.d("trndwcs", "repeat song")
                    musicService.repeatSong(true)
                    musicService.repeatPlaylist(false)
                }
            }
        }
    }

    private fun getDominantColor(bitmap: Bitmap?): GradientDrawable {
        try {
            val swatchesTemp = Palette.from(bitmap!!).generate().swatches
            val swatches: List<Swatch> = ArrayList(swatchesTemp)
            Collections.sort(
                swatches
            ) { swatch1, swatch2 -> swatch2.population - swatch1.population }
            val gd: GradientDrawable
            when (swatches.size) {
                0 ->
                    gd = GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        intArrayOf(Color.BLACK, Color.BLACK)
                    )

                1 ->
                    gd = GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        intArrayOf(swatches[0].rgb, Color.BLACK)
                    )

                else ->
                    gd = GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        intArrayOf(swatches[1].rgb, swatches[0].rgb)
                    )

            }
            gd.cornerRadius = 0f
            return gd
        } catch (e: Exception) {
            e.printStackTrace()
            return GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(Color.WHITE, Color.BLACK)
            )
        }
    }

    private fun prepareBundle(songItem: PlaylistSongItem) {
        bundle = Bundle()
        bundle.putInt("song_position", musicService.songPosition)
        bundle.putParcelableArrayList("song_list", ArrayList(musicService.songList))
        bundle.putParcelableArrayList("shuffle_song_list", ArrayList(musicService.shuffleSongList))
        bundle.putParcelable("song_item", songItem)
        intent = Intent(context, MusicService::class.java)
        intent.putExtra("song_bundle", bundle)
    }

    private fun startMusicService() {
        // stop previous song
        musicService.reset()
        context?.stopService(intent)
        context?.unbindService(mServiceConnection)
        //start next song
        context?.startService(intent)
        context?.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)
        dialogBinding.btnPlay.setImageResource(R.drawable.ic_pause)
    }

    private fun initSeekBar() {
        dialogBinding.seekBar.progress = musicService.currentPosition()
        dialogBinding.seekBar.max = musicService.duration()
        var amoungToupdate = musicService.duration() / 100
        musicService.songDuration.observe(viewLifecycleOwner) {duration ->
            dialogBinding.tvTotalTime.text = musicTimer.setTimer(duration)
            dialogBinding.seekBar.max = duration
            amoungToupdate = duration / 100
        }
        dialogBinding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    musicService.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        handler.postDelayed(object : Runnable {
            override fun run() {
                dialogBinding.seekBar.progress = musicService.currentPosition()
                dialogBinding.tvCurrentTime.text =
                    musicTimer.setTimer(musicService.currentPosition())
                handler.postDelayed(this, 1000)
            }
        }, 0)
    }

    private fun disableButtonForOneSecond(button : ImageButton) {
        button.isEnabled = false // Disable the button

        // Re-enable the button after 1000 milliseconds (1 second)
        Handler().postDelayed({
            button.isEnabled = true // Enable the button
        }, 500)
    }
}