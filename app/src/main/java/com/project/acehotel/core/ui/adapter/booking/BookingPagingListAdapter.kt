package com.project.acehotel.core.ui.adapter.booking

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.project.acehotel.core.domain.booking.model.Booking
import com.project.acehotel.core.utils.DateUtils
import com.project.acehotel.core.utils.constants.mapToRoomDisplay
import com.project.acehotel.databinding.ItemListBookingBinding

class BookingPagingListAdapter : PagingDataAdapter<Booking, BookingPagingListAdapter.ViewHolder>(
    DIFF_CALLBACK
) {
    private lateinit var onItemCallback: OnItemClickCallback

    inner class ViewHolder(val binding: ItemListBookingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)

        holder.binding.apply {
            tvBookingName.text = data?.visitorName
            tvBookingRoomCount.text = "${data?.roomCount} kamar"
            tvBookingRoomType.text = mapToRoomDisplay(data?.type ?: "type")
            tvBookingNightCount.text = "${data?.duration} malam"
            tvBookingDate.text =
                "${DateUtils.convertDate(data?.checkinDate!!)} - ${DateUtils.convertDate(data.checkoutDate)}"
        }

        holder.itemView.setOnClickListener {
            onItemCallback.onItemClicked(holder.itemView.context, data!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListBookingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(context: Context, booking: Booking)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Booking>() {
            override fun areItemsTheSame(oldItem: Booking, newItem: Booking): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Booking, newItem: Booking): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}