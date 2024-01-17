package com.project.acehotel.features.dashboard.management.inventory.chooseitem

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.inventory.model.Inventory
import com.project.acehotel.core.ui.adapter.inventory.InventoryListAdapter
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.ActivityChooseItemInventoryBinding
import com.project.acehotel.features.dashboard.management.inventory.additem.AddItemInventoryActivity
import com.project.acehotel.features.dashboard.management.inventory.changestock.ChangeStockItemInventoryActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ChooseItemInventoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseItemInventoryBinding

    private val chooseItemViewModel: ChooseItemViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChooseItemInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        handleButtonBack()

        fetchInventoryItems()

        handleOnRefresh()

        handleButtonAddItems()

        showLoading(true)
    }

    override fun onResume() {
        super.onResume()

        showLoading(true)
    }

    private fun handleButtonBack() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun handleButtonAddItems() {
        binding.btnAddItem.setOnClickListener {
            val itemId = intent.getStringExtra(INVENTORY_ITEM_ID)
            val itemName = intent.getStringExtra(INVENTORY_ITEM_NAME)
            val itemType = intent.getStringExtra(INVENTORY_ITEM_TYPE)

            val intentToAddItem = Intent(this, AddItemInventoryActivity::class.java)
            intentToAddItem.putExtra(INVENTORY_ITEM_ID, itemId)
            intentToAddItem.putExtra(INVENTORY_ITEM_NAME, itemName)
            intentToAddItem.putExtra(INVENTORY_ITEM_TYPE, itemType)

            startActivity(intentToAddItem)
        }
    }

    private fun handleOnRefresh() {
        binding.refInventoryItem.setOnRefreshListener {
            fetchInventoryItems()
        }
    }

    private fun fetchInventoryItems() {
        chooseItemViewModel.getListInventory().observe(this) { item ->
            when (item) {
                is Resource.Error -> {
                    showLoading(false)

                    if (!isInternetAvailable(this)) {
                        showToast(getString(R.string.check_internet))
                    } else {
                        showToast(item.message.toString())
                    }
                }
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Message -> {
                    showLoading(false)
                    Timber.tag("InventoryDetailActivity").d(item.message)
                }
                is Resource.Success -> {
                    showLoading(false)

                    if (item.data != null) {
                        initInventoryItemRecyclerView(item.data)
                    }
                }
            }
        }
    }

    private fun initInventoryItemRecyclerView(inventoryItem: List<Inventory>) {
        val adapter = InventoryListAdapter(inventoryItem)
        binding.rvListItemInventory.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        binding.rvListItemInventory.layoutManager = layoutManager

        adapter.setOnItemClickCallback(object : InventoryListAdapter.OnItemClickCallback {
            override fun onItemClicked(context: Context, id: String, name: String, type: String) {
                val intentToChangeStockInventory =
                    Intent(
                        this@ChooseItemInventoryActivity,
                        ChangeStockItemInventoryActivity::class.java
                    )

                intentToChangeStockInventory.putExtra(INVENTORY_ITEM_ID, id)
                intentToChangeStockInventory.putExtra(INVENTORY_ITEM_NAME, name)
                intentToChangeStockInventory.putExtra(INVENTORY_ITEM_TYPE, type)

                startActivity(intentToChangeStockInventory)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.refInventoryItem.isRefreshing = isLoading
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }

    companion object {
        private const val INVENTORY_ITEM_ID = "inventory_item_id"
        private const val INVENTORY_ITEM_NAME = "inventory_item_name"
        private const val INVENTORY_ITEM_TYPE = "inventory_item_type"
    }
}