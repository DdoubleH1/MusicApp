package com.example.baseproject.ui.playlist.customplaylist

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.data.model.LibraryItem
import com.example.baseproject.databinding.CustomPlaylistDialogBinding
import com.example.baseproject.databinding.DialogPlaylistItemBinding
import com.example.baseproject.ui.playlist.PlaylistViewModel
import com.example.core.utils.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CustomPLaylistDialog(
    allPlaylist: MutableList<LibraryItem>,
    private var songId: Int
) :
    BottomSheetDialogFragment(),
    OnItemClickListener {
    private lateinit var dialogBinding: CustomPlaylistDialogBinding
    private lateinit var playlistDiaLogAdapter: PlaylistDialogAdapter
    private val viewModel: PlaylistViewModel by activityViewModels()
    fun getVM() = viewModel
    private var playlistList = allPlaylist
    private lateinit var cPlaylistList: MutableList<String>
    private lateinit var oPlaylistList: List<String>
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
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialogBinding = CustomPlaylistDialogBinding.inflate(inflater, container, false)


        viewModel.tPlaylistListId.observe(viewLifecycleOwner) {
            cPlaylistList = it.toMutableList()
            oPlaylistList = it
            playlistDiaLogAdapter =
                PlaylistDialogAdapter(playlistList.toMutableList(), cPlaylistList, this)
            dialogBinding.rcvListPlaylist.adapter = playlistDiaLogAdapter
        }

        dialogBinding.rcvListPlaylist.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        dialogBinding.btnDone.setOnClickListener {
            viewModel.reset(cPlaylistList, oPlaylistList, songId)
            dismiss()
        }

        return dialogBinding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.cPlaylist.observe(viewLifecycleOwner) {
            viewModel.getSong(it.playlistId)
        }
    }

    override fun onItemClicked(playlistId: String, view: DialogPlaylistItemBinding) {
        view.btnAction.isChecked.toString().toast(requireContext())
        if (view.btnAction.isChecked && playlistId !in cPlaylistList)
            cPlaylistList.add(playlistId)
        else if (!view.btnAction.isChecked && playlistId in cPlaylistList)
            cPlaylistList.remove(playlistId)

    }
}