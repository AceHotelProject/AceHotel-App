package com.project.acehotel.features.dashboard.management.inventory.choose_item

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
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
import com.project.acehotel.features.dashboard.management.inventory.add_item.AddItemInventoryActivity
import com.project.acehotel.features.dashboard.management.inventory.change_stock.ChangeStockItemInventoryActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ChooseItemInventoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseItemInventoryBinding

    private val chooseItemViewModel: ChooseItemViewModel by viewModels()
    private var hotelId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChooseItemInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        handleButtonBack()

        fetchInventoryItems("")

        handleOnRefresh()

        handleButtonAddItems()

        setupSearch()

        showLoading(true)
    }

    private fun setupSearch() {
        binding.edInventoryItemSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                fetchInventoryItems(p0.toString())
            }

        })

        binding.edInventoryItemSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                fetchInventoryItems(binding.edInventoryItemSearch.text.toString())
                true // consume the action
            } else {
                false // pass on to other listeners.
            }
        }
    }

    private fun getHotelId() {
        chooseItemViewModel.getSelectedHotel().observe(this) { hotel ->
            hotelId = hotel
        }
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
            fetchInventoryItems("")
        }
    }

    private fun fetchInventoryItems(filter: String) {
        var type = ""
        var name = ""

        if (filter == "linen" || filter == "Linen") {
            type = "linen"
        } else if (filter == "Kasur" || filter == "kasur") {
            type = "kasur"
        } else {
            name = filter
        }

        chooseItemViewModel.fetchListInventory(name, type).observe(this) { item ->
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
            override fun onItemClicked(
                context: Context,
                id: String,
                name: String,
                type: String,
                stock: Int
            ) {
                val intentToChangeStockInventory =
                    Intent(
                        this@ChooseItemInventoryActivity,
                        ChangeStockItemInventoryActivity::class.java
                    )

                intentToChangeStockInventory.putExtra(INVENTORY_ITEM_ID, id)
                intentToChangeStockInventory.putExtra(INVENTORY_ITEM_NAME, name)
                intentToChangeStockInventory.putExtra(INVENTORY_ITEM_TYPE, type)
                intentToChangeStockInventory.putExtra(INVENTORY_ITEM_STOCK, stock)

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
        private const val INVENTORY_ITEM_STOCK = "inventory_item_stock"
    }
}