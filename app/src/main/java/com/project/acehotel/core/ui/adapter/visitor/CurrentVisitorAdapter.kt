package com.project.acehotel.core.ui.adapter.visitor

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.ui.custom.cards.CustomCardCurrentVisitor
import com.project.acehotel.core.utils.DateUtils
import com.project.acehotel.core.utils.constants.CurrentVisitorStatus

class CurrentVisitorAdapter(private val listCurrentVisitor: List<Booking>?) :
    RecyclerView.Adapter<CurrentVisitorAdapter.ViewHolder>() {

    private lateinit var onItemCallback: OnItemClickCallback

    inner class ViewHolder(val binding: CustomCardCurrentVisitor) :
        RecyclerView.ViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CustomCardCurrentVisitor(parent.context)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listCurrentVisitor?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listCurrentVisitor?.get(position)

        if (data != null) {
            holder.binding.apply {
                setVisitorName(data.visitorName)
                setVisitorDate(
                    "${DateUtils.convertDate(data?.checkinDate!!)} - ${
                        DateUtils.convertDate(
                            data.checkoutDate
                        )
                    }"
                )

                if (data.room.isNotEmpty()) {
                    if (data.room.first().actualCheckin != "Empty" && data.room.first().actualCheckout != "Empty") {
                        val getCurrentTime =
                            DateUtils.convertTimeToHourMinute(data.room.first().actualCheckout)

                        setVisitorStatus(CurrentVisitorStatus.CHECKOUT.status, getCurrentTime, true)
                    } else if (data.room.first().actualCheckin != "Empty" && DateUtils.isTodayDate(
                            data.checkoutDate
                        )
                    ) {
                        setVisitorStatus(CurrentVisitorStatus.CHECKOUT.status, "", false)
                    } else if (data.room.first().actualCheckin != "Empty" && !DateUtils.isTodayDate(
                            data.checkoutDate
                        )
                    ) {
                        val getCurrentTime =
                            DateUtils.convertTimeToHourMinute(data.room.first().actualCheckin)

                        setVisitorStatus(CurrentVisitorStatus.CHECKIN.status, getCurrentTime, true)
                    } else {
                        setVisitorStatus(CurrentVisitorStatus.CHECKIN.status, "", false)
                    }
                } else {
                    setVisitorStatus(CurrentVisitorStatus.CHECKIN.status, "", false)
                }
            }

            holder.itemView.setOnClickListener {
                onItemCallback.onItemClicked(data.visitorId, data.visitorName)
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(id: String, name: String)
    }
}