package com.project.acehotel.core.ui.custom.chips

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.project.acehotel.R
import com.project.acehotel.core.utils.constants.CurrentVisitorStatus

class CustomBookingStatus : AppCompatTextView {

    private var state: CurrentVisitorStatus = CurrentVisitorStatus.CHECKIN
    private var isDone: Boolean = false

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
        text = "Status"

        setBackground(context, R.color.dark_grey)
    }

    fun setStatus(status: CurrentVisitorStatus, isDone: Boolean) {
        this.state = status
        this.isDone = isDone

        updateStatus()
    }

    private fun updateStatus() {
        when (state) {
            CurrentVisitorStatus.CHECKIN -> {
                if (isDone) {
                    text = "Checkin"
                    setBackground(context, R.color.green)
                } else {
                    text = "Belum Checkin"
                    setBackground(context, R.color.dark_grey)
                }
            }
            CurrentVisitorStatus.CHECKOUT -> {
                if (isDone) {
                    text = "Checkout"
                    setBackground(context, R.color.red)
                } else {
                    text = "Belum Checkou"
                    setBackground(context, R.color.dark_grey)
                }
            }
            else -> {
                text = "Status"
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