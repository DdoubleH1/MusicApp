package com.example.baseproject.ui.playlist.editplaylist

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.data.model.LibraryItem
import com.example.baseproject.databinding.PlaylistEditDialogBinding
import com.example.baseproject.ui.playlist.PlaylistViewModel
import com.example.core.utils.toast
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditPlaylistDialog(
    private val playlist: LibraryItem
) :
    BottomSheetDialogFragment(),
    OnItemClickListener {
    private lateinit var dialogBinding: PlaylistEditDialogBinding
    private val removeList: MutableList<Int> = mutableListOf()
    private var newImage: String? = null
    private val viewModel: PlaylistViewModel by activityViewModels()
    fun getVM() = viewModel
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
        dialogBinding = PlaylistEditDialogBinding.inflate(inflater, container, false)

        dialogBinding.edtTitle.setText(playlist.playlistTitle)

        if (playlist.playlistImage != null)
            Glide.with(requireContext())
                .load(playlist.playlistImage!!.toUri())
                .into(dialogBinding.imgPlaylistCover)
        else
            dialogBinding.imgPlaylistCover.setImageResource(R.drawable.spotify)

        viewModel.songList.observe(viewLifecycleOwner) {
            val editDiaLogAdapter =
                EditPlaylistDialogAdapter(it.toMutableList(), this)
            dialogBinding.rcvPlaylistSong.adapter = editDiaLogAdapter
        }
        dialogBinding.rcvPlaylistSong.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        requireActivity().supportFragmentManager.setFragmentResultListener(
            "new_image",
            viewLifecycleOwner
        ) { key, bundle ->
            if (key == "new_image") {
                val uriString = bundle.getString("new_image")
                dialogBinding.imgPlaylistCover.setImageURI(uriString?.toUri())
            }
        }

        clickFunction()

        return dialogBinding.root
    }

    private fun clickFunction() {
        dialogBinding.cancelAction.setOnClickListener {
            dismiss()
        }

        dialogBinding.saveAction.setOnClickListener {
            val newTitle = dialogBinding.edtTitle.text.toString()
            viewModel.edit(removeList, playlist.playlistId, newTitle, newImage)
            viewModel.getSong(playlist.playlistId)
            dismiss()
        }

        dialogBinding.imgPlaylistCover.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)
                .cropSquare()
                .maxResultSize(
                    1080,
                    1080
                )
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }
    }


    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!

                newImage = fileUri.toString()
                dialogBinding.imgPlaylistCover.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                ImagePicker.getError(data).toast(requireContext())
            } else {
                "Task Cancelled".toast(requireContext())
            }
        }

    override fun onRemoveClicked(itemId: Int) {
        removeList.add(itemId)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.getSong(playlist.playlistId)
    }
}