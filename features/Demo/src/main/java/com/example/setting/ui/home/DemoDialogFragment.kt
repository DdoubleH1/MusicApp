package com.example.setting.ui.home

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

const val DemoDialogFragmentTag = "DemoDialogFragmentTag"

class DemoDialogFragment : DialogFragment() {

    private var demoDialogListener: DemoDialogListener? = null

    fun setDemoDialogListener(demoDialogListener: DemoDialogListener) {
        this.demoDialogListener = demoDialogListener
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment != null && parentFragment is DemoDialogListener) {
            setDemoDialogListener(parentFragment as DemoDialogListener)
            return
        }

        if (context is DemoDialogListener) {
            setDemoDialogListener(context as DemoDialogListener)
            return
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Title")
            .setMessage("Content")
            .setPositiveButton("Ok") { _, _ ->
                demoDialogListener?.onClickOk()
            }
            .setNegativeButton("Cancel") { _, _ ->
                demoDialogListener?.onClickCancel()
            }
        return builder.create()
    }
}

interface DemoDialogListener {
    fun onClickOk()
    fun onClickCancel()
}