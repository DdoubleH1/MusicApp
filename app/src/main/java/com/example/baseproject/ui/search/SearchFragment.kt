package com.example.baseproject.ui.search

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentSearchBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SearchFragment :
    BaseFragment<FragmentSearchBinding, SearchViewModel>(R.layout.fragment_search) {

    @Inject
    lateinit var appNavigation: AppNavigation

    companion object {
        fun newInstance() = SearchFragment()
    }

    private val viewModel: SearchViewModel by viewModels()
    override fun getVM() = viewModel
    private val categoryList = dummyCategoryList()
    private val searchCategoryAdapter = CategoryAdapter(categoryList.toMutableList())

    override fun setOnClick() {
        super.setOnClick()
        searchCategoryAdapter.onItemClick = {
            val bundle = Bundle()

            bundle.putString("categoryTitle", it.itemTitle)
            this.findNavController()
                .navigate(R.id.action_searchFragment_to_playlistFragment, bundle)
        }
    }

    override fun bindingStateView() {
        super.bindingStateView()
        binding.rcvSearch.adapter = searchCategoryAdapter
        binding.rcvSearch.layoutManager = GridLayoutManager(requireContext(), 2)
    }


    private fun dummyCategoryList(): List<SearchCategoryItem> {
        val itemList: MutableList<SearchCategoryItem> = ArrayList()
        val item1 = SearchCategoryItem("Pop")
        val item2 = SearchCategoryItem("Bollywood")
        val item3 = SearchCategoryItem("Podcasts")
        val item4 = SearchCategoryItem("Pop")
        val item5 = SearchCategoryItem("Pop")
        val item6 = SearchCategoryItem("Pop")
        val item7 = SearchCategoryItem("Pop")
        val item8 = SearchCategoryItem("Pop")
        val item9 = SearchCategoryItem("Pop")
        val item10 = SearchCategoryItem("Pop")
        val item11 = SearchCategoryItem("Pop")
        val item12 = SearchCategoryItem("Pop")
        itemList.add(item1)
        itemList.add(item2)
        itemList.add(item3)
        itemList.add(item4)
        itemList.add(item5)
        itemList.add(item6)
        itemList.add(item7)
        itemList.add(item8)
        itemList.add(item9)
        itemList.add(item10)
        itemList.add(item11)
        itemList.add(item12)
        return itemList
    }


}