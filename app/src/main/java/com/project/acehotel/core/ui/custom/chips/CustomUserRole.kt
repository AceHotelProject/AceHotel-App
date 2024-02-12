package com.project.acehotel.core.ui.custom.chips

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.project.acehotel.R
import com.project.acehotel.core.utils.constants.UserRole

class CustomUserRole : AppCompatTextView {
    private var status: String = UserRole.UNDEFINED.role

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        text = "Role"
        setBackground(context, R.color.dark_grey)
    }

    fun setStatus(status: String) {
        this.status = status
        updateStatus()
    }

    private fun updateStatus() {
        when (status) {
            UserRole.ADMIN.role -> {
                text = UserRole.ADMIN.display
                setBackground(context, R.color.red)
            }
            UserRole.MASTER.role -> {
                text = UserRole.MASTER.display
                setBackground(context, R.color.blue)
            }
            UserRole.FRANCHISE.role -> {
                text = UserRole.FRANCHISE.display
                setBackground(context, R.color.dark_purple)
            }
            UserRole.RECEPTIONIST.role -> {
                text = UserRole.RECEPTIONIST.display
                setBackground(context, R.color.purple)
            }
            UserRole.INVENTORY_STAFF.role -> {
                text = UserRole.INVENTORY_STAFF.display
                setBackground(context, R.color.orange)
            }
            UserRole.CLEANING_STAFF.role -> {
                text = UserRole.CLEANING_STAFF.display
                setBackground(context, R.color.green)
            }
            UserRole.UNDEFINED.role -> {
                text = UserRole.UNDEFINED.display
                setBackground(context, R.color.dark_grey)
            }
        }
    }

    private fun setBackground(context: Context, colorId: Int) {
        setTextColor(ContextCompat.getColor(context, colorId))
        setBackgroundColor(
            ColorUtils.setAlphaComponent(
                ContextCompat.getColor(
                    context,
                    colorId,
                ), ALPHA_OPACITY
            )
        )
    }

    companion object {
        const val ALPHA_OPACITY = 64
    }
}