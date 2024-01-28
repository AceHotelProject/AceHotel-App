package com.project.acehotel.core.ui.custom.cards

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.project.acehotel.R
import com.project.acehotel.core.utils.constants.CurrentVisitorStatus

class CustomCardVisitor(context: Context, attributeSet: AttributeSet) :
    CardView(context, attributeSet) {

    init {
        LayoutInflater.from(context).inflate(R.layout.item_list_current_visitor, this, true)
    }

    fun setVisitorName(name: String) {
        findViewById<TextView>(R.id.tv_current_visitor_name).text = name
    }

    fun setVisitorDate(date: String) {
        findViewById<TextView>(R.id.tv_current_visitor_date).text = date
    }

    fun setVisitorStatus(status: String, time: String, isDone: Boolean) {
        when (status) {
            CurrentVisitorStatus.CHECKIN.status -> {
                if (isDone) {
                    statusDisplay(
                        time, R.color.green, R.drawable.icons_visitor_card_checkin_true
                    )
                } else {
                    statusDisplay(
                        "Pengunjung belum checkin",
                        R.color.dark_grey,
                        R.drawable.icons_visitor_card_checkin_false
                    )
                }
            }
            CurrentVisitorStatus.CHECKOUT.status -> {
                if (isDone) {
                    statusDisplay(
                        time, R.color.red, R.drawable.icons_visitor_card_checkout_true
                    )
                } else {
                    statusDisplay(
                        "Pengunjung belum checkout",
                        R.color.dark_grey,
                        R.drawable.icons_visitor_card_checkout_false
                    )
                }
            }
        }
    }

    private fun statusDisplay(time: String, colorId: Int, imageId: Int) {
        findViewById<TextView>(R.id.tv_current_visitor_desc).apply {
            text = time
            setTextColor(colorId)
        }

        findViewById<ImageView>(R.id.iv_current_visitor_status).setImageResource(imageId)
    }
}