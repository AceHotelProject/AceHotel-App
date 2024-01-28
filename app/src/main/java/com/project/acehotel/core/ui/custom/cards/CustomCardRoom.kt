package com.project.acehotel.core.ui.custom.cards

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.project.acehotel.R
import com.project.acehotel.core.ui.custom.chips.CustomRoomType
import com.project.acehotel.core.utils.constants.RoomStatus
import com.project.acehotel.core.utils.constants.RoomType

class CustomCardRoom(context: Context, attributes: AttributeSet) : CardView(context, attributes) {
    private var status: String = RoomType.UNDEFINED.display

    init {
        LayoutInflater.from(context).inflate(R.layout.item_list_room, this, true)

        setRoomStatus(RoomStatus.UNDEFINED.status)
    }

    fun setRoomNumber(number: Int) {
        findViewById<TextView>(R.id.tv_room_card_name).text = "Kamar $number"
    }

    fun setRoomType(type: RoomType) {
        findViewById<CustomRoomType>(R.id.tv_room_card_status).setStatus(type.type)
    }

    fun setRoomStatus(status: String) {
        this.status = status

        updateStatus()
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