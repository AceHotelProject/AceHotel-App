package com.project.acehotel.core.ui.adapter.booking.callback

import androidx.recyclerview.widget.DiffUtil
import com.project.acehotel.core.domain.booking.model.Booking
import timber.log.Timber

class BookingPagingDiffCallback : DiffUtil.ItemCallback<Booking>() {
    override fun areItemsTheSame(oldItem: Booking, newItem: Booking): Boolean {
        Timber.tag("DIFF").e(oldItem.toString())
        Timber.tag("DIFF").e(newItem.toString())
        Timber.tag("DIFF").e("Result areItemsTheSame ${oldItem == newItem}")

        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Booking, newItem: Booking): Boolean {
        Timber.tag("DIFF").e(oldItem.toString())
        Timber.tag("DIFF").e(newItem.toString())
        Timber.tag("DIFF").e("Result areContentsTheSame ${oldItem == newItem}")

        return oldItem.id == newItem.id
    }
}