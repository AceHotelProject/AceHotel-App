package com.project.acehotel.core.ui.custom

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
                text = UserRole.ADMIN.role
                setBackground(context, R.color.red)
            }
            UserRole.FRANCHISE.role -> {
                text = UserRole.FRANCHISE.role
                setBackground(context, R.color.dark_purple)
            }
            UserRole.EMPLOYEE.role -> {
                text = UserRole.EMPLOYEE.role
                setBackground(context, R.color.purple)
            }
            UserRole.MASTER.role -> {
                text = UserRole.ADMIN.role
                setBackground(context, R.color.blue)
            }
            UserRole.UNDEFINED.role -> {
                text = UserRole.UNDEFINED.role
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