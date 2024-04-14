package com.project.acehotel.core.ui.adapter.visitor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.acehotel.core.domain.visitor.model.Visitor
import com.project.acehotel.databinding.ItemListVisitorBinding

class VisitorListAdapter(private val listVisitor: List<Visitor>?) :
    RecyclerView.Adapter<VisitorListAdapter.ViewHolder>() {

    private lateinit var onItemCallback: OnItemClickCallback

    inner class ViewHolder(val binding: ItemListVisitorBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListVisitorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listVisitor?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listVisitor?.get(position)

        holder.binding.apply {
            tvVisitorCardName.text = data?.name
            tvVisitorCardPhone.text = "Nomor telepon: ${data?.phone}"
        }

        holder.itemView.setOnClickListener {
            if (data != null) {
                onItemCallback.onItemClicked(data.id, data.name)
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