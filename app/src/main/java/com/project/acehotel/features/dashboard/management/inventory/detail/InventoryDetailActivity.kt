package com.project.acehotel.features.dashboard.management.inventory.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.inventory.model.Inventory
import com.project.acehotel.core.domain.inventory.model.InventoryHistory
import com.project.acehotel.core.ui.adapter.inventory.InventoryHistoryAdapter
import com.project.acehotel.core.utils.DateUtils
import com.project.acehotel.core.utils.constants.DeleteDialogType
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.ActivityInventoryDetailBinding
import com.project.acehotel.features.dashboard.management.inventory.changestock.ChangeStockItemInventoryActivity
import com.project.acehotel.features.dashboard.management.inventory.chooseitem.ChooseItemInventoryActivity
import com.project.acehotel.features.popup.delete.DeleteItemDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class InventoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInventoryDetailBinding
    private val inventoryDetailViewModel: InventoryDetailViewModel by viewModels()

    private lateinit var itemId: String
    private lateinit var itemName: String
    private lateinit var itemType: String
    private var itemStock: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInventoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        handleFab()

        handleBackButton()

        fetchInventoryDetail()

        handleButtonMore()

        getItemInfo()
    }

    private fun getItemInfo() {
        if (intent.getStringExtra(INVENTORY_ITEM_ID) != null) {
            itemId = intent.getStringExtra(INVENTORY_ITEM_ID).toString()
            itemName = intent.getStringExtra(INVENTORY_ITEM_NAME).toString()
            itemType = intent.getStringExtra(INVENTORY_ITEM_TYPE).toString()
        }
    }

    private fun handleButtonMore() {
        binding.btnInventoryDetailMore.setOnClickListener {
            val popUpMenu = PopupMenu(this, binding.btnInventoryDetailMore)
            popUpMenu.menuInflater.inflate(R.menu.menu_detail_item, popUpMenu.menu)

            popUpMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menuUpdate -> {
                        val intentToUpdateItem =
                            Intent(this, ChangeStockItemInventoryActivity::class.java)

                        intentToUpdateItem.putExtra(FLAG_UPDATE, true)
                        intentToUpdateItem.putExtra(INVENTORY_ITEM_ID, itemId)
                        intentToUpdateItem.putExtra(INVENTORY_ITEM_NAME, itemName)
                        intentToUpdateItem.putExtra(INVENTORY_ITEM_TYPE, itemType)
                        intentToUpdateItem.putExtra(INVENTORY_ITEM_STOCK, itemStock)

                        startActivity(intentToUpdateItem)
                        true
                    }
                    R.id.menuDelete -> {
                        DeleteItemDialog(DeleteDialogType.INVENTORY_DETAIL, itemId).show(
                            supportFragmentManager,
                            "Delete Dialog"
                        )
                        true
                    }
                    else -> false
                }
            }

            popUpMenu.show()
        }
    }

    private fun fetchInventoryDetail() {
        val itemId = intent.getStringExtra(INVENTORY_ITEM_ID)

        if (!itemId.isNullOrEmpty()) {
            inventoryDetailViewModel.getDetailInventory(itemId).observe(this) { inventory ->
                when (inventory) {
                    is Resource.Error -> {
                        showLoading(false)

                        if (!isInternetAvailable(this)) {
                            showToast(getString(R.string.check_internet))
                        } else {
                            showToast(inventory.message.toString())
                        }
                    }
                    is Resource.Loading -> {
                        showLoading(true)
                    }
                    is Resource.Message -> {
                        showLoading(false)
                        Timber.tag("InventoryDetailActivity").d(inventory.message)
                    }
                    is Resource.Success -> {
                        showLoading(false)

                        if (inventory.data != null) {
                            setupDetailInfo(inventory.data)

                            initInventoryHistoryRecyclerView(inventory.data.historyList)
                        }
                    }
                }
            }
        }
    }

    private fun initInventoryHistoryRecyclerView(historyList: List<InventoryHistory>) {
        val adapter = InventoryHistoryAdapter(historyList)
        binding.rvListHistoryInventory.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        binding.rvListHistoryInventory.layoutManager = layoutManager
    }

    private fun setupDetailInfo(data: Inventory?) {
        if (data != null) {
            binding.apply {
                tvInventoryDetailName.text = data.name
                tvInventoryCardStock.text = data.stock.toString()
                tvInventoryDetailDesc.text =
                    "Perubahan " + DateUtils.convertDate(data.historyList.last().date)
                chipInventoryCardType.setStatus(data.type)

                itemStock = data.stock
            }
        }
    }

    private fun handleBackButton() {
        binding.btnInventoryDetailBack.setOnClickListener {
            finish()
        }
    }

    private fun handleFab() {
        binding.fabInventoryDetailChangeStock.setOnClickListener {
            val intentToChooseItem = Intent(this, ChooseItemInventoryActivity::class.java)
            intentToChooseItem.putExtra(INVENTORY_ITEM_ID, itemId)
            intentToChooseItem.putExtra(INVENTORY_ITEM_NAME, itemName)
            intentToChooseItem.putExtra(INVENTORY_ITEM_TYPE, itemType)

            startActivity(intentToChooseItem)
        }
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val INVENTORY_ITEM_ID = "inventory_item_id"
        private const val INVENTORY_ITEM_NAME = "inventory_item_name"
        private const val INVENTORY_ITEM_TYPE = "inventory_item_type"
        private const val INVENTORY_ITEM_STOCK = "inventory_item_stock"

        private const val FLAG_UPDATE = "inventory_flag_update"
    }
}