package com.project.acehotel.core.ui.custom.cards

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.project.acehotel.R
import com.project.acehotel.core.ui.custom.chips.CustomRoomType
import com.project.acehotel.core.utils.constants.RoomStatus
import com.project.acehotel.core.utils.constants.RoomType

class CustomCardRoom(context: Context) : CardView(context) {
    private var status: String = RoomType.UNDEFINED.display

    init {
        LayoutInflater.from(context).inflate(R.layout.item_list_room, this, true)

        setRoomStatus(RoomStatus.UNDEFINED.status)
    }

    fun setRoomNumber(name: String) {
        findViewById<TextView>(R.id.tv_room_card_name).text = name
    }

    fun setRoomType(type: String) {
        findViewById<CustomRoomType>(R.id.chip_room_card_type).setStatus(type)
    }

    fun setRoomStatus(status: String) {
        this.status = status

        updateStatus()
    }

    fun setCardMargin(position: Int) {
        val cardView = findViewById<CardView>(R.id.cd_room)
        val layoutParams = cardView.layoutParams as ConstraintLayout.LayoutParams

        val dpValue = 8 // For example, you want to set all margins to 16dp
        val density = resources.displayMetrics.density
        val marginInPx = (dpValue * density).toInt()

        val marginBottomInPx = (16 * density).toInt()

        if (position % 2 == 0) {
//            layoutParams.setMargins(marginInPx, 0, 0, marginBottomInPx)
            layoutParams.setMargins(0, 0, marginInPx, marginBottomInPx)
        } else {
//            layoutParams.setMargins(0, 0, marginInPx, marginBottomInPx)
            layoutParams.setMargins(marginInPx, 0, 0, marginBottomInPx)
        }
    }

    private fun updateStatus() {
        when (status) {
            RoomStatus.READY.status -> {
                statusDisplay(
                    RoomStatus.READY.display,
                    R.color.green,
                    R.drawable.icons_room_card_avail
                )
            }
            RoomStatus.USED.status -> {
                statusDisplay(
                    RoomStatus.USED.display,
                    R.color.red,
                    R.drawable.icons_room_card_not_avail
                )
            }
            RoomStatus.BROKEN.status -> {
                statusDisplay(
                    RoomStatus.READY.display,
                    R.color.dark_grey,
                    R.drawable.icons_room_card_broken
                )
            }
            else -> {
                statusDisplay(
                    RoomStatus.UNDEFINED.display,
                    R.color.dark_grey,
                    R.drawable.icons_room_card_broken
                )
            }
        }
    }

    private fun statusDisplay(display: String, colorId: Int, imageId: Int) {
        findViewById<TextView>(R.id.tv_room_card_status).apply {
            text = display
            setTextColor(colorId)
        }

        findViewById<ImageView>(R.id.iv_room_card_status).setImageResource(imageId)
    }


}