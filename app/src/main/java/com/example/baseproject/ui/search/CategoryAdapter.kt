package com.example.baseproject.ui.search

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.R
import com.example.baseproject.databinding.SearchCategoryBinding
import com.example.baseproject.navigation.ItemClickNavigation


class CategoryAdapter(private val categoryList: MutableList<SearchCategoryItem>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    var onItemClick: ((SearchCategoryItem) -> Unit)? = null

    inner class CategoryViewHolder(itemView: SearchCategoryBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val categoryTitle: TextView = itemView.searchCategory
        val card: CardView = itemView.categoryCard
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val mSearchCategoryItem: SearchCategoryBinding =
            SearchCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CategoryViewHolder(mSearchCategoryItem)
    }


    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.categoryTitle.text = category.itemTitle
        holder.card.setOnClickListener {
            onItemClick?.invoke(category)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}

