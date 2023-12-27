package com.project.acehotel.core.ui.custom

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.project.acehotel.R
import com.project.acehotel.core.utils.constants.InventoryType

class CustomInventoryType : AppCompatTextView {
    private var status: String = InventoryType.UNDEFINED.type

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
            InventoryType.LINEN.type -> {
                text = InventoryType.LINEN.type
                setBackground(context, R.color.orange)
            }
            InventoryType.BED.type -> {
                text = InventoryType.BED.type
                setBackground(context, R.color.yellow)
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