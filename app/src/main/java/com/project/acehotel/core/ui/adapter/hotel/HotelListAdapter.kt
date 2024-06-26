package com.project.acehotel.core.ui.adapter.hotel

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.acehotel.core.domain.hotel.model.ChooseHotel
import com.project.acehotel.core.domain.hotel.model.ManageHotel
import com.project.acehotel.databinding.ItemListChooseHotelBinding

class HotelListAdapter(
    private val manageHotel: List<ManageHotel>?,
    private val selectedHotel: ManageHotel
) :
    RecyclerView.Adapter<HotelListAdapter.ViewHolder>() {

    private lateinit var onItemCallback: OnItemClickCallback
    private var chooseHotelData = manageHotel?.map { hotel ->
        ChooseHotel(hotel, selectedHotel.id == hotel.id)
    } ?: emptyList()

    inner class ViewHolder(val binding: ItemListChooseHotelBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListChooseHotelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = manageHotel?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = chooseHotelData[position]

        holder.binding.apply {
            tvChooseHotelName.text = data.hotel.name
            tvChooseHotelAddress.text = data.hotel.address
            radioButton.isChecked = data.isChecked
        }

        holder.itemView.setOnClickListener {
            onItemCallback.onItemClicked(holder.itemView.context, data.hotel)

            updateCheckedPosition(position)

            notifyDataSetChanged()
        }
    }

    private fun updateCheckedPosition(position: Int) {
        for (i in chooseHotelData.indices) {
            chooseHotelData[i].isChecked = (i == position)
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(context: Context, data: ManageHotel)
    }
}