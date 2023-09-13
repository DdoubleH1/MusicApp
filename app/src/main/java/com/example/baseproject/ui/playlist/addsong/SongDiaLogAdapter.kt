package com.example.baseproject.ui.playlist.addsong

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.data.model.PlaylistSongItem
import com.example.baseproject.databinding.DialogSongItemBinding
import com.example.core.utils.toast

interface OnItemClickListener {
    fun onItemClicked(itemId: Int, view: DialogSongItemBinding)
    fun onAddClicked(position: Int, itemId: Int, view: DialogSongItemBinding)
}

class SongDiaLogAdapter(
    private var mSongList: MutableList<PlaylistSongItem>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<SongDiaLogAdapter.PlaylistSongItemViewHolder>() {

    fun setFilteredList(mList: List<PlaylistSongItem>) {
        this.mSongList = mList.toMutableList()
        notifyDataSetChanged()
    }

    inner class PlaylistSongItemViewHolder(
        mPlaylistSongItem: DialogSongItemBinding,
        onItemClickListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(mPlaylistSongItem.root) {
        val songItemTitle: TextView = mPlaylistSongItem.songTitle
        val songItemArtist: TextView = mPlaylistSongItem.songArtist
        val songImage: ImageView = mPlaylistSongItem.songImageView
        val addBtn: ImageButton = mPlaylistSongItem.add

        init {
            mPlaylistSongItem.apply {
                add.setOnClickListener {
                    onItemClickListener.onAddClicked(
                        adapterPosition,
                        mSongList[adapterPosition].songId,
                        mPlaylistSongItem
                    )
                    it.isClickable = false
                    this@SongDiaLogAdapter.mSongList.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistSongItemViewHolder {
        val mPlaylistSongItem: DialogSongItemBinding = DialogSongItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PlaylistSongItemViewHolder(mPlaylistSongItem, onItemClickListener)
    }

    override fun onBindViewHolder(holder: PlaylistSongItemViewHolder, position: Int) {
        val item = mSongList[position]
        holder.songItemTitle.text = item.songTitle
        holder.songItemArtist.text = item.artists
        holder.songImage.setImageURI(item.songImage?.toUri())
        holder.addBtn.isClickable = true
    }

    override fun getItemCount(): Int {
        return mSongList.size
    }
}