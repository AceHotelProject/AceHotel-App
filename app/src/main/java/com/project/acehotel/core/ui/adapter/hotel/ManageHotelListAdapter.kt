package com.project.acehotel.core.ui.adapter.hotel

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.project.acehotel.R
import com.project.acehotel.core.domain.hotel.model.ManageHotel
import com.project.acehotel.core.utils.constants.DeleteDialogType
import com.project.acehotel.databinding.ItemListHotelFranchiseBinding
import com.project.acehotel.features.dashboard.profile.add_franchise.AddFranchiseActivity
import com.project.acehotel.features.popup.delete.DeleteItemDialog

class ManageHotelListAdapter(
    private val manageHotel: List<ManageHotel>?,
    private val supportFragmentManager: FragmentManager
) :
    RecyclerView.Adapter<ManageHotelListAdapter.ViewHolder>() {

    private lateinit var onItemCallback: OnItemClickCallback

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

    override fun getItemCount(): Int = manageHotel?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = manageHotel?.get(position)

        holder.binding.apply {
            tvFranchiseHotelName.text = data?.name
            tvFranchiseHotelAddress.text = data?.address

            tvFranchiseHotelOwner.text = "${data?.ownerName} - ${data?.ownerEmail}"
            tvFranchiseHotelReceptionist.text =
                "${data?.receptionistName} - ${data?.receptionistEmail}"
            tvFranchiseHotelInventory.text =
                "${data?.inventoryStaffName} - ${data?.inventoryStaffEmail}"
        }

        holder.binding.btnFranchiseMore.setOnClickListener {
            val popUpMenu = PopupMenu(holder.itemView.context, holder.binding.btnFranchiseMore)
            popUpMenu.menuInflater.inflate(R.menu.menu_detail_item, popUpMenu.menu)

            popUpMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menuUpdate -> {
                        holder.itemView.context.apply {
                            val intentToAUpdateHotel =
                                Intent(this, AddFranchiseActivity::class.java)
                            intentToAUpdateHotel.putExtra(FLAG_HOTEL_UI, FLAG_HOTEL_UPDATE)
                            intentToAUpdateHotel.putExtra(HOTEL_ID, data?.id)

                            startActivity(intentToAUpdateHotel)
                        }
                        true
                    }
                    R.id.menuDelete -> {
                        if (data != null) {
                            DeleteItemDialog(DeleteDialogType.MANAGE_HOTEL, data.id).show(
                                supportFragmentManager,
                                "Delete Dialog"
                            )
                        }
                        true
                    }
                    else -> false
                }
            }

            popUpMenu.show()
        }

        holder.itemView.setOnClickListener {
            holder.itemView.context.apply {
                val intentToManageHotelDetail =
                    Intent(this, AddFranchiseActivity::class.java)
                intentToManageHotelDetail.putExtra(FLAG_HOTEL_UI, FLAG_HOTEL_DETAIL)
                intentToManageHotelDetail.putExtra(HOTEL_ID, data?.id)

                startActivity(intentToManageHotelDetail)
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(context: Context, id: String)
    }

    companion object {
        private const val FLAG_HOTEL_UI = "flag_hotel_UI"
        private const val FLAG_HOTEL_DETAIL = "hotel_detail"
        private const val FLAG_HOTEL_UPDATE = "hotel_update"

        private const val HOTEL_ID = "hotel_id"
    }
}