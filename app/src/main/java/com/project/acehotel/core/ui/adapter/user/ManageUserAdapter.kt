package com.project.acehotel.core.ui.adapter.user

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.acehotel.core.domain.auth.model.User
import com.project.acehotel.databinding.ItemListUserBinding
import com.project.acehotel.features.dashboard.profile.update_user.UpdateUserActivity

class ManageUserAdapter(private val listUser: List<User>?, private val context: Context) :
    RecyclerView.Adapter<ManageUserAdapter.ViewHolder>() {

    private lateinit var onItemCallback: OnItemClickCallback

    inner class ViewHolder(val binding: ItemListUserBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(context: Context, id: String, holder: ItemListUserBinding)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listUser?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listUser?.get(position)

        holder.binding.apply {
            tvUserCardRole.text = data?.role?.role
            tvUserCardName.text = data?.username
            tvUserCardEmail.text = data?.email
        }

        holder.binding.ibDeleteUser.setOnClickListener {
            if (data != null) {
                onItemCallback.onItemClicked(
                    holder.itemView.context,
                    data.id!!,
                    holder.binding
                )
            }
        }

        holder.binding.ibUpdateUser.setOnClickListener {
            val intentToUpdateUser = Intent(holder.itemView.context, UpdateUserActivity::class.java)
            holder.itemView.context.startActivity(intentToUpdateUser)
        }
    }
}