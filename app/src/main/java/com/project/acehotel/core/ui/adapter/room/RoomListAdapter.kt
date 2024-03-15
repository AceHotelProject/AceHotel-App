package com.project.acehotel.core.ui.adapter.room

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.domain.room.model.Room
import com.project.acehotel.core.ui.custom.cards.CustomCardRoom
import com.project.acehotel.core.utils.DateUtils
import com.project.acehotel.core.utils.constants.RoomStatus

class RoomListAdapter(private val listRoom: List<Room>?) :
    RecyclerView.Adapter<RoomListAdapter.ViewHolder>() {

    private lateinit var onItemCallback: OnItemClickCallback

    inner class ViewHolder(val binding: CustomCardRoom) : RecyclerView.ViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CustomCardRoom(parent.context)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listRoom?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listRoom?.get(position)

        if (data != null) {
            holder.binding.apply {
                setRoomNumber(data.name)
                setRoomType(data.type)

                if (!data.isBooked) {
                    setRoomStatus(RoomStatus.READY.status)
                } else {
                    if (!isRoomBookings(data.bookings)) {
                        setRoomStatus(RoomStatus.READY.status)
                    } else {
                        setRoomStatus(RoomStatus.USED.status)
                    }
                }

                setCardMargin(position)
            }
        }

        holder.itemView.setOnClickListener {
            if (data != null) {
                onItemCallback.onItemClicked(holder.itemView.context, data)
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(context: Context, room: Room)
    }
}

private fun isRoomBookings(bookings: List<Booking>): Boolean {
    for (booking in bookings) {
        return DateUtils.isTodayDate(booking.checkinDate)
    }

    return true
}