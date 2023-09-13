package com.example.baseproject.ui.library

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.data.model.LibraryItem
import com.example.baseproject.databinding.LibraryItemBinding
import com.example.baseproject.navigation.ItemClickNavigation

class LibraryItemAdapter(
    private var libraryItemList: List<LibraryItem>,
    private val onItemClickListener: ItemClickNavigation
) : RecyclerView.Adapter<LibraryItemAdapter.LibraryItemViewHolder>() {

    private lateinit var myListener: ItemClickNavigation
    fun setOnItemClickListener(listener: ItemClickNavigation) {
        myListener = listener
    }

    inner class LibraryItemViewHolder(
        mLibraryItem: LibraryItemBinding
    ) : RecyclerView.ViewHolder(mLibraryItem.root) {
        var libraryItemTitle = mLibraryItem.libraryTitle
        val libraryItemImage: ImageView = mLibraryItem.imgPlaylist

        init {
            mLibraryItem.libraryItem.setOnClickListener {
                onItemClickListener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryItemViewHolder {
        val mLibraryItem: LibraryItemBinding =
            LibraryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LibraryItemViewHolder(mLibraryItem)
    }

    override fun getItemCount(): Int {
        return libraryItemList.size
    }

    override fun onBindViewHolder(holder: LibraryItemViewHolder, position: Int) {
        val item = libraryItemList[position]
        holder.libraryItemTitle.text = item.playlistTitle
        val context = holder.libraryItemImage.context
        if (item.playlistImage != null) {
            val uri = item.playlistImage!!.toUri().buildUpon().scheme("https").build()
            Glide.with(context)
                .load(uri)
                .into(holder.libraryItemImage)
        } else {
            holder.libraryItemImage.setImageResource(R.drawable.spotify)
        }
    }
}