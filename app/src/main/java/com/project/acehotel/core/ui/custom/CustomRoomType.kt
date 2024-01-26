package com.project.acehotel.core.ui.custom

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.project.acehotel.R
import com.project.acehotel.core.utils.constants.RoomType

class CustomRoomType : AppCompatTextView {
    private var status: String = RoomType.UNDEFINED.type

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
        text = "Type"
        setBackground(context, R.color.dark_grey)
    }

    fun setStatus(status: String) {
        this.status = status
        updateStatus()
    }

    private fun updateStatus() {
        when (status) {
            RoomType.REGULAR.type -> {
                text = RoomType.REGULAR.display
                setBackground(context, R.color.blue)
            }
            RoomType.EXCLUSIVE.type -> {
                text = RoomType.EXCLUSIVE.display
                setBackground(context, R.color.dark_purple)
            }
            else -> {
                text = RoomType.UNDEFINED.display
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