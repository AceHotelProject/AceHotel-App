package com.project.acehotel.features.popup.delete

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.project.acehotel.R
import com.project.acehotel.core.utils.constants.DeleteDialogType
import com.project.acehotel.core.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteItemDialog(private val deleteDialogType: DeleteDialogType, private val id: String) :
    DialogFragment() {
    private val deleteItemViewModel: DeleteItemViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it, R.style.TransparentDialogTheme)
            val inflater = requireActivity().layoutInflater

            val view = inflater.inflate(R.layout.dialog_delete_item, null)
            builder.setView(view)
            builder.setCancelable(false)

            val tvDesc = view.findViewById<TextView>(R.id.tv_delete_desc)
            val btnYes = view.findViewById<Button>(R.id.btn_delete_yes)
            val btnNo = view.findViewById<Button>(R.id.btn_delete_no)

            when (deleteDialogType) {
                DeleteDialogType.INVENTORY_DETAIL -> {
                    tvDesc.text = "Apakah Anda yakin ingin menghapus barang ini?"
                    btnYes.setOnClickListener {
                        deleteItemViewModel.deleteInventory(id).observe(this) {
                            activity?.showToast("Barang telah berhasi dihapus")
                            activity?.finish()
                            dismiss()
                        }
                    }
                    btnNo.setOnClickListener {
                        dismiss()
                    }
                }
                DeleteDialogType.VISITOR_DETAIL -> {
                    tvDesc.text = "Apakah Anda yakin ingin menghapus barang ini?"
                    btnYes.setOnClickListener {

                    }
                    btnNo.setOnClickListener {
                        dismiss()
                    }
                }
                DeleteDialogType.BOOKING_DETAIL -> {
                    tvDesc.text = "Apakah Anda yakin ingin menghapus barang ini?"
                    btnYes.setOnClickListener {

                    }
                    btnNo.setOnClickListener {
                        dismiss()
                    }
                }
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}