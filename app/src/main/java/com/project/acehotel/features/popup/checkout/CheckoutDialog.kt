package com.project.acehotel.features.popup.checkout

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.project.acehotel.R
import com.project.acehotel.features.dashboard.booking.choose_booking.ChooseBookingActivity

class CheckoutDialog(private val noCheckoutCount: Int) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it, R.style.TransparentDialogTheme)
            val inflater = requireActivity().layoutInflater

            val view = inflater.inflate(R.layout.dialog_reminder_checkout, null)
            builder.setView(view)
            builder.setCancelable(false)

            val textReminder = view.findViewById<TextView>(R.id.tv_reminder_checkout)
            textReminder.text =
                "Terdapat ${noCheckoutCount} pengunjung yang belum checkout,\n" +
                        "pastikan kembali data booking terkini"

            val btnContinue = view.findViewById<Button>(R.id.btn_expired_continue)
            btnContinue.setOnClickListener {

                val intentToChooseBooking =
                    Intent(requireContext(), ChooseBookingActivity::class.java)
                intentToChooseBooking.putExtra(
                    FLAG_VISITOR,
                    MENU_CHECKOUT
                )
                startActivity(intentToChooseBooking)

                dismiss()
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        private const val FLAG_VISITOR = "flag_visitor"
        private const val MENU_CHECKOUT = "checkout"
    }
}