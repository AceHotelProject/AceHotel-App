package com.project.acehotel.features.popup.delete

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.utils.constants.DeleteDialogType
import com.project.acehotel.core.utils.isInternetAvailable
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

            btnNo.setOnClickListener {
                dismiss()
            }

            when (deleteDialogType) {
                DeleteDialogType.INVENTORY_DETAIL -> {
                    tvDesc.text = "Apakah Anda yakin ingin menghapus barang ini?"
                    btnYes.setOnClickListener {
                        deleteItemViewModel.executeDeleteInventory(id).observe(this) { result ->
                            when (result) {
                                is Resource.Error -> {
                                    if (!isInternetAvailable(requireContext())) {
                                        activity?.showToast(getString(R.string.check_internet))
                                    } else {
                                        activity?.showToast(result.message.toString())
                                    }
                                }
                                is Resource.Loading -> {

                                }
                                is Resource.Message -> {

                                }
                                is Resource.Success -> {
                                    activity?.showToast("Barang telah berhasi dihapus")
                                    activity?.finish()
                                    dismiss()
                                }
                            }
                        }
                    }
                }
                DeleteDialogType.VISITOR_DETAIL -> {
                    tvDesc.text = "Apakah Anda yakin ingin menghapus pengunjung ini?"
                    btnYes.setOnClickListener {

                    }
                }
                DeleteDialogType.BOOKING_DETAIL -> {
                    tvDesc.text = "Apakah Anda yakin ingin menghapus booking ini?"
                    btnYes.setOnClickListener {

                    }
                }
                DeleteDialogType.MANAGE_HOTEL -> {
                    tvDesc.text = "Apakah Anda yakin ingin menghapus cabang hotel ini?"
                    btnYes.setOnClickListener {
                        deleteItemViewModel.getSelectedHotel().observe(this) { saveId ->
                            if (id == saveId) {
                                deleteItemViewModel.saveSelectedHotel("")
                            }
                        }

                        deleteItemViewModel.executeDeleteHotel(id).observe(this) { result ->
                            when (result) {
                                is Resource.Error -> {
                                    if (!isInternetAvailable(requireContext())) {
                                        activity?.showToast(getString(R.string.check_internet))
                                    } else {
                                        activity?.showToast(result.message.toString())
                                    }
                                }
                                is Resource.Loading -> {

                                }
                                is Resource.Message -> {

                                }
                                is Resource.Success -> {
                                    activity?.showToast("Cabang hotel telah berhasi dihapus")
                                    activity?.finish()
                                    dismiss()
                                }
                            }
                        }
                    }
                }
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}