package com.example.baseproject.ui.home.hometab

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentHomeTabBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.utils.Response
import com.example.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeTabFragment :
    BaseFragment<FragmentHomeTabBinding, HomeTabViewModel>(R.layout.fragment_home_tab) {

    var mList = ParentItemList()
    private val parentAdapter = ParentAdapter(mList)

    @Inject
    lateinit var appNavigation: AppNavigation
    private val viewModel: HomeTabViewModel by viewModels()
    override fun getVM() = viewModel
    override fun setOnClick() {
        super.setOnClick()
        binding.ProgressBar.visibility= android.view.View.VISIBLE
        parentAdapter.onItemClick = { parentItem: ParentItem, childItem: ChildItem ->
            val bundle = Bundle()

            val title = parentItem.parentItemTitle + "\n" + childItem.childItemTitle
            bundle.putString("title", title)
            this.findNavController()
                .navigate(R.id.action_homeTabFragment_to_playlistFragment, bundle)

        }
    }

    override fun bindingStateView() {
        super.bindingStateView()
        viewModel.profileResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> {}
                is Response.Failure -> {}
                is Response.Success -> {
                    Glide.with(requireContext())
                        .load(response.data.profilePictureUrl)
                        .into(binding.imgProfile)
                    binding.titleTv.text = getString(R.string.hello) + " ${response.data.name}"
                    binding.ProgressBar.visibility= android.view.View.GONE
                }
            }
        }
        binding.rcvFrg.adapter = parentAdapter
        binding.rcvFrg.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun ParentItemList(): List<ParentItem> {
        val itemList: MutableList<ParentItem> = ArrayList()
        val item = ParentItem(
            "Title 1",
            ChildItemList()
        )
        itemList.add(item)
        val item1 = ParentItem(
            "Title 2",
            ChildItemList()
        )
        itemList.add(item1)
        val item2 = ParentItem(
            "Title 3",
            ChildItemList()
        )
        itemList.add(item2)
        val item3 = ParentItem(
            "Title 4",
            ChildItemList()
        )
        itemList.add(item3)
        return itemList
    }

    private fun ChildItemList(): List<ChildItem> {
        val ChildItemList: MutableList<ChildItem> = ArrayList()
        ChildItemList.add(ChildItem("Card 1"))
        ChildItemList.add(ChildItem("Card 2"))
        ChildItemList.add(ChildItem("Card 3"))
        ChildItemList.add(ChildItem("Card 4"))
        return ChildItemList
    }
}