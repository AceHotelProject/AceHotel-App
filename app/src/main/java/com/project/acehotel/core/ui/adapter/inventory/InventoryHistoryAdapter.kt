package com.project.acehotel.core.ui.adapter.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.acehotel.R
import com.project.acehotel.core.domain.inventory.model.InventoryHistory
import com.project.acehotel.core.utils.DateUtils
import com.project.acehotel.databinding.ItemListHistoryInventoryBinding

class InventoryHistoryAdapter(private val listInventoryHistory: List<InventoryHistory>) :
    RecyclerView.Adapter<InventoryHistoryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemListHistoryInventoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InventoryHistoryAdapter.ViewHolder {
        val binding = ItemListHistoryInventoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InventoryHistoryAdapter.ViewHolder, position: Int) {
        val data = listInventoryHistory[position]

        holder.binding.apply {
            tvInventoryHistoryCardDate.text = DateUtils.convertDate(data.date)
            tvInventoryHistoryName.text = data.title
            tvInventoryHistoryDesc.text = data.desc
            tvInventoryHistoryStock.text = data.stockChange.toString()
            tvInventoryHistoryPic.text = "oleh ${data.personInCharge}"

            if (data.stockChange < 0) {
                ivInventoryHistoryStockStatus.setImageResource(R.drawable.icons_inventory_history_decreased)
            } else {
                ivInventoryHistoryStockStatus.setImageResource(R.drawable.icons_inventory_history_increased)
            }
        }
    }

    override fun getItemCount(): Int {
        return listInventoryHistory.size ?: 0
    }

}