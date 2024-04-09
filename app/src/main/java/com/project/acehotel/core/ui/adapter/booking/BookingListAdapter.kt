package com.project.acehotel.core.ui.adapter.booking

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.utils.DateUtils
import com.project.acehotel.core.utils.constants.mapToRoomDisplay
import com.project.acehotel.core.utils.formatNumber
import com.project.acehotel.databinding.ItemListBookingBinding

class BookingListAdapter(private val listBooking: List<Booking>?) :
    RecyclerView.Adapter<BookingListAdapter.ViewHolder>() {

    private lateinit var onItemCallback: OnItemClickCallback

    inner class ViewHolder(val binding: ItemListBookingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListBookingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listBooking?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listBooking?.get(position)

        holder.binding.apply {
            tvBookingName.text = data?.visitorName
            tvBookingRoomCount.text = "${data?.roomCount} kamar"
            tvBookingRoomType.text = mapToRoomDisplay(data?.type ?: "type")
            tvBookingNightCount.text = "${data?.duration} malam"
            tvBookingDate.text =
                "${DateUtils.convertDate(data?.checkinDate!!)} - ${DateUtils.convertDate(data.checkoutDate)}"
            tvBookingPrice.text = "Rp ${formatNumber(data.totalPrice)}"
        }

        holder.itemView.setOnClickListener {
            onItemCallback.onItemClicked(holder.itemView.context, data!!)
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(context: Context, booking: Booking)
    }
}