package com.project.acehotel.core.ui.adapter.hotel

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.acehotel.core.domain.hotel.model.ChooseHotel
import com.project.acehotel.core.domain.hotel.model.Hotel
import com.project.acehotel.databinding.ItemListChooseHotelBinding

class HotelListAdapter(private val listHotel: List<Hotel>?) :
    RecyclerView.Adapter<HotelListAdapter.ViewHolder>() {

    private lateinit var onItemCallback: OnItemClickCallback
    private var chooseHotelData = listHotel?.map { hotel ->
        ChooseHotel(hotel, false)
    } ?: emptyList()

    inner class ViewHolder(val binding: ItemListChooseHotelBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListChooseHotelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listHotel?.size ?: 0

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = chooseHotelData[position]

        holder.binding.apply {
            tvChooseHotelName.text = data.hotel.name
            tvChooseHotelAddress.text = data.hotel.address
            radioButton.isChecked = data.isChecked
        }

        holder.itemView.setOnClickListener {
            onItemCallback.onItemClicked(holder.itemView.context, data.hotel.id)

            updateCheckedPosition(position)
            notifyDataSetChanged()
        }
    }

    private fun updateCheckedPosition(
        position: Int,
    ) {
        for (i in chooseHotelData.indices) {
            chooseHotelData[i].isChecked = (i == position)
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(context: Context, id: String)
    }
}