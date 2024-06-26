package com.project.acehotel.core.ui.adapter.inventory

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.acehotel.core.domain.inventory.model.Inventory
import com.project.acehotel.core.utils.DateUtils
import com.project.acehotel.databinding.ItemListInventoryBinding
import timber.log.Timber

class InventoryListAdapter(private val listInventory: List<Inventory>?) :
    RecyclerView.Adapter<InventoryListAdapter.ViewHolder>() {

    private lateinit var onItemCallback: OnItemClickCallback

    inner class ViewHolder(val binding: ItemListInventoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListInventoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listInventory?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listInventory?.get(position)

        holder.binding.apply {
            if (data != null) {
                Timber.tag("INVENTORY").e(data.historyList.last().date)

                chipInventoryCardType.setStatus(data.type)
                tvInventoryCardName.text = data.name
                tvInventoryCardDesc.text =
                    "Perubahan terakhir:  " + DateUtils.convertDate(data.historyList.last().date)
                tvInventoryCardStock.text = data.stock.toString()
            }
        }

        holder.itemView.setOnClickListener {
            if (data != null) {
                onItemCallback.onItemClicked(
                    holder.itemView.context,
                    data.id,
                    data.name,
                    data.type,
                    data.stock
                )
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(context: Context, id: String, name: String, type: String, stock: Int)
    }
}