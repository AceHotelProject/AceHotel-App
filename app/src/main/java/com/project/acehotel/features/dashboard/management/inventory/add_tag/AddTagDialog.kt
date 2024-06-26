package com.project.acehotel.features.dashboard.management.inventory.add_tag

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.utils.showLongToast
import com.project.acehotel.features.dashboard.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddTagDialog(
    private val inventoryId: String,
    private val tagId: String,
    private val inventoryName: String
) :
    DialogFragment() {
    private val addTagViewModel: AddTagViewModel by activityViewModels()

    private var btnLogoutNo: Button? = null
    private var btnLogoutYes: Button? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it, R.style.TransparentDialogTheme)
            val inflater = requireActivity().layoutInflater

            val view = inflater.inflate(R.layout.dialog_add_tag, null)
            builder.setView(view)
            builder.setCancelable(false)

            btnLogoutNo = view?.findViewById<Button>(R.id.btn_add_tag_no)
            btnLogoutYes = view?.findViewById<Button>(R.id.btn_add_tag_yes)

            val addTagText = view?.findViewById<TextView>(R.id.tv_add_tag)
            addTagText?.text = "Apakah anda yakin ingin menyimpan data tag pada $inventoryName"

            btnLogoutNo?.setOnClickListener {
                dismiss()
            }

            btnLogoutYes?.setOnClickListener {
                handleAddTagButton()
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun handleAddTagButton() {
        addTagViewModel.addTag(tagId, inventoryId).observe(this@AddTagDialog) { tag ->
            when (tag) {
                is Resource.Error -> {
                    dismiss()

                    isButtoNenabled(false)
                    activity?.showLongToast("Gagal menambahkan tag")
                }
                is Resource.Loading -> {
                    isButtoNenabled(false)
                }
                is Resource.Message -> {
                    dismiss()

                    isButtoNenabled(false)
                    activity?.showLongToast("Gagal menambahkan tag")
                }
                is Resource.Success -> {
                    isButtoNenabled(true)

                    dismiss()
                    activity?.showLongToast("Berhasil menambahkan tag")
                    val intentToMain = Intent(requireContext(), MainActivity::class.java)
                    activity?.startActivity(intentToMain)
                }
            }
        }
    }

    private fun isButtoNenabled(isEnabled: Boolean) {
        btnLogoutYes?.isEnabled = isEnabled
        btnLogoutNo?.isEnabled = isEnabled
    }

    companion object {
        private const val READER_NAME = "ACE-001"
    }
}