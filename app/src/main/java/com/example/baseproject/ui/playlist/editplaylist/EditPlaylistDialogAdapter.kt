package com.example.baseproject.ui.playlist.editplaylist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.data.model.PlaylistSongItem
import com.example.baseproject.databinding.EditPlaylistRcvItemBinding

interface OnItemClickListener {

    fun onRemoveClicked(itemId: Int)
}

class EditPlaylistDialogAdapter(
    private var mSongList: MutableList<PlaylistSongItem>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<EditPlaylistDialogAdapter.PlaylistSongItemViewHolder>() {

    inner class PlaylistSongItemViewHolder(
        mPlaylistSongItem: EditPlaylistRcvItemBinding,
        onItemClickListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(mPlaylistSongItem.root) {
        val songTitle: TextView = mPlaylistSongItem.tvTitle
        val songArtist: TextView = mPlaylistSongItem.tvArtist
        val removeBtn: ImageButton = mPlaylistSongItem.btnDelete

        init {
            mPlaylistSongItem.apply {
                btnDelete.setOnClickListener {
                    onItemClickListener.onRemoveClicked(
                        mSongList[adapterPosition].songId
                    )
                    it.isClickable = false
                    this@EditPlaylistDialogAdapter.mSongList.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistSongItemViewHolder {
        val mPlaylistSongItem: EditPlaylistRcvItemBinding = EditPlaylistRcvItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PlaylistSongItemViewHolder(mPlaylistSongItem, onItemClickListener)
    }

    override fun onBindViewHolder(holder: PlaylistSongItemViewHolder, position: Int) {
        val item = mSongList[position]
        holder.songTitle.text = item.songTitle
        holder.songArtist.text = item.artists
        holder.removeBtn.isClickable = true
    }

    override fun getItemCount(): Int {
        return mSongList.size
    }
}