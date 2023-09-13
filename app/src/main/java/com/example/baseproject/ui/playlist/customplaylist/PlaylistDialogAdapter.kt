package com.example.baseproject.ui.playlist.customplaylist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.R
import com.example.baseproject.data.model.LibraryItem
import com.example.baseproject.databinding.DialogPlaylistItemBinding


interface OnItemClickListener {

    fun onItemClicked(playlistId: String, view: DialogPlaylistItemBinding)

}

class PlaylistDialogAdapter(
    private val mPlaylistList: MutableList<LibraryItem>,
    private val cPlaylistList: MutableList<String>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<PlaylistDialogAdapter.PlaylistViewHolder>() {

    inner class PlaylistViewHolder(
        mItem: DialogPlaylistItemBinding,
        onItemClickListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(mItem.root) {
        val playlistTitle: TextView = mItem.playlistTitle
        val customBtn: ToggleButton = mItem.btnAction

        init {
            mItem.apply {
                btnAction.setOnClickListener {

                    onItemClickListener.onItemClicked(
                        mPlaylistList[adapterPosition].playlistId,
                        mItem
                    )

                }
            }
            mItem.btnAction.text = null
            mItem.btnAction.textOn = null
            mItem.btnAction.textOff = null
            mItem.btnAction.setBackgroundResource(R.drawable.my_btn_toggle)
        }
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val item = mPlaylistList[position]
        holder.playlistTitle.text = item.playlistTitle

        holder.customBtn.isChecked = item.playlistId in cPlaylistList

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val mPlaylistItem: DialogPlaylistItemBinding = DialogPlaylistItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PlaylistViewHolder(mPlaylistItem, onItemClickListener)
    }

    override fun getItemCount(): Int {
        return mPlaylistList.size
    }
}