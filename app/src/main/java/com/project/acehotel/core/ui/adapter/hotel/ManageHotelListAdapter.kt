package com.project.acehotel.core.ui.adapter.hotel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.acehotel.core.domain.hotel.model.ListHotel
import com.project.acehotel.databinding.ItemListHotelFranchiseBinding

class ManageHotelListAdapter(private val listHotel: List<ListHotel>?) :
    RecyclerView.Adapter<ManageHotelListAdapter.ViewHolder>() {

    private lateinit var onItemCallback: HotelListAdapter.OnItemClickCallback

    inner class ViewHolder(val binding: ItemListHotelFranchiseBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListHotelFranchiseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listHotel?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listHotel?.get(position)

        holder.binding.apply {
            tvFranchiseHotelName.text = data?.name
            tvFranchiseHotelAddress.text = data?.address
        }
    }
}