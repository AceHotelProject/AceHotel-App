package com.project.acehotel.features.dashboard.profile

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.project.acehotel.R
import com.project.acehotel.features.splash.SplashActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LogoutDialog : DialogFragment() {
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it, R.style.TransparentDialogTheme)
            val inflater = requireActivity().layoutInflater

            val view = inflater.inflate(R.layout.dialog_logout, null)
            builder.setView(view)
            builder.setCancelable(false)

            val btnLogoutNo = view?.findViewById<Button>(R.id.btn_logout_no)
            val btnLogoutYes = view?.findViewById<Button>(R.id.btn_logout_yes)

            btnLogoutNo?.setOnClickListener {
                dismiss()
            }

            btnLogoutYes?.setOnClickListener {
                logoutUser()
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun logoutUser() {
        profileViewModel.getUser().observe(this) { user ->

            if (user != null) {
                Timber.tag("TEST").e(user.toString())
                profileViewModel.deleteUser(user)

                val intentToSplash = Intent(requireContext(), SplashActivity::class.java)
                startActivity(intentToSplash)
                
            }
        }
    }
}