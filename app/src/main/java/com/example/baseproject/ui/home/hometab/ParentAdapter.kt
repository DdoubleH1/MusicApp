package com.example.baseproject.ui.home.hometab

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.databinding.ParentLayoutBinding


class ParentAdapter(private val ParentItemList: List<ParentItem>) :
    RecyclerView.Adapter<ParentAdapter.ParentViewHolder>() {

    var onItemClick: ((ParentItem, ChildItem) -> Unit)? = null

    inner class ParentViewHolder(itemView: ParentLayoutBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var ParentItemTitle: TextView = itemView.parentTitle
        val ChildRecyclerView: RecyclerView = itemView.childRcv
    }

    private val viewPool = RecyclerView.RecycledViewPool()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val mParentItem: ParentLayoutBinding =
            ParentLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParentViewHolder(mParentItem)
    }

    override fun getItemCount(): Int {
        return ParentItemList.size
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        val parent = ParentItemList[position]
        holder.ParentItemTitle.text = parent.parentItemTitle
        val layoutManager = LinearLayoutManager(
            holder.ChildRecyclerView
                .context,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        layoutManager.initialPrefetchItemCount = parent
            .childItemList
            .size
        val childItemAdapter = ChildAdapter(
            parent
                .childItemList
        )

        childItemAdapter.onItemClick = {
            val cItem = it
            onItemClick?.invoke(ParentItemList[position], cItem)
        }

        holder.ChildRecyclerView.layoutManager = layoutManager
        holder.ChildRecyclerView.adapter = childItemAdapter
        holder.ChildRecyclerView
            .setRecycledViewPool(viewPool)
    }
}