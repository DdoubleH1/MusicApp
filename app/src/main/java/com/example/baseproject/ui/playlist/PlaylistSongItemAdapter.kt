package com.example.baseproject.ui.playlist

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.R
import com.example.baseproject.data.model.PlaylistSongItem
import com.example.baseproject.databinding.PlaylistSongItemBinding
import java.io.File


interface OnItemClickListener {
    fun onItemClicked(item: PlaylistSongItem, view: PlaylistSongItemBinding)
}

class PlaylistSongItemAdapter(
    private var mSongList: List<PlaylistSongItem>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<PlaylistSongItemAdapter.PlaylistSongItemViewHolder>() {

    fun setFilteredList(mList: List<PlaylistSongItem>) {
        this.mSongList = mList
        notifyDataSetChanged()
    }

    inner class PlaylistSongItemViewHolder(
        mPlaylistSongItem: PlaylistSongItemBinding,
        onItemClickListener: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(mPlaylistSongItem.root) {
        val songItemTitle: TextView = mPlaylistSongItem.songTitle
        val songItemArtist: TextView = mPlaylistSongItem.songArtist
        val songImage: ImageView = mPlaylistSongItem.songImageView

        init {
            mPlaylistSongItem.apply {
                playlistSongItem.setOnClickListener {
                    onItemClickListener.onItemClicked(
                        mSongList[adapterPosition],
                        mPlaylistSongItem
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistSongItemViewHolder {
        val mPlaylistSongItem: PlaylistSongItemBinding = PlaylistSongItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PlaylistSongItemViewHolder(mPlaylistSongItem, onItemClickListener)
    }

    override fun onBindViewHolder(holder: PlaylistSongItemViewHolder, position: Int) {
        val item = mSongList[position]
        holder.songItemTitle.text = item.songTitle
        holder.songItemArtist.text = item.artists
        try {
            holder.songImage.setImageURI(item.songImage?.toUri())
        } catch (e: Exception) {
            e.printStackTrace()
            holder.songImage.setImageResource(R.drawable.ic_music)
        }
//        holder.songImage.setImageURI(item.songImage?.toUri())
    }

    override fun getItemCount(): Int {
        return mSongList.size
    }
}