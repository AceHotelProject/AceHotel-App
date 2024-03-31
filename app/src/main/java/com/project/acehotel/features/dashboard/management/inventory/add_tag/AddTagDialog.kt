package com.project.acehotel.features.dashboard.management.inventory.add_tag

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.features.dashboard.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AddTagDialog(private val id: String, private val inventorName: String) : DialogFragment() {
    private val addTagViewModel: AddTagViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it, R.style.TransparentDialogTheme)
            val inflater = requireActivity().layoutInflater

            val view = inflater.inflate(R.layout.dialog_add_tag, null)
            builder.setView(view)
            builder.setCancelable(false)

            val btnLogoutNo = view?.findViewById<Button>(R.id.btn_add_tag_no)
            val btnLogoutYes = view?.findViewById<Button>(R.id.btn_add_tag_yes)

            val addTagText = view?.findViewById<TextView>(R.id.tv_add_tag)
            addTagText?.text = "Apakah anda yakin ingin menyimpan data tag pada $inventorName"

            btnLogoutNo?.setOnClickListener {
                dismiss()
            }

            btnLogoutYes?.setOnClickListener {
                showLoading(true)

                addTagViewModel.executeAddTag(READER_NAME, id).observe(requireActivity()) { tag ->
                    when (tag) {
                        is Resource.Error -> {
                            showLoading(false)
                            Timber.tag("AddTagDialog").e(tag.message)
                        }
                        is Resource.Loading -> {
                            showLoading(true)
                        }
                        is Resource.Message -> {
                            showLoading(false)
                            Timber.tag("AddTagDialog").d(tag.message)

                        }
                        is Resource.Success -> {
                            showLoading(false)

                            val intentToMain = Intent(requireContext(), MainActivity::class.java)
                            activity?.startActivity(intentToMain)
                            dismiss()
                        }
                    }
                }
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun showLoading(isLoading: Boolean) {
        val loading = view?.findViewById<ProgressBar>(R.id.pb_add_tag)

        loading?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val READER_NAME = "ACE-001"
    }
}