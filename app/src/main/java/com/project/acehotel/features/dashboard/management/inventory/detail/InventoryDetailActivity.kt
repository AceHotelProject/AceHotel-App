package com.project.acehotel.features.dashboard.management.inventory.detail

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.project.acehotel.features.dashboard.management.inventory.change_stock.ChangeStockItemInventoryActivity
import com.project.acehotel.features.dashboard.management.inventory.choose_item.ChooseItemInventoryActivity
import com.project.acehotel.features.popup.delete.DeleteItemDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class InventoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInventoryDetailBinding
    private val inventoryDetailViewModel: InventoryDetailViewModel by viewModels()

    private var hotelId: String = ""

    private lateinit var itemId: String
    private lateinit var itemName: String
    private lateinit var itemType: String
    private var itemStock: Int = 0

    private var savedHistoryData: List<InventoryHistory> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInventoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        binding.tvEmptyInventoryHistory.visibility = View.VISIBLE

        handleFab()

        getHotelId()

        handleBackButton()

        fetchInventoryDetail()

        handleButtonMore()

        getItemInfo()

        setupSearch()

        handleRefresh()
    }

    private fun handleRefresh() {
        binding.apply {
            rvListHistoryInventory.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                    val firstVisibleItemPosition =
                        layoutManager?.findFirstVisibleItemPosition() ?: 0

                    binding.refInventoryDetail.isEnabled = firstVisibleItemPosition == 0
                }
            })

            refInventoryDetail.setOnRefreshListener {
                fetchInventoryDetail()
            }
        }
    }

    private fun setupSearch() {
        binding.edInventoryDetailSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrEmpty()) {
                    initInventoryHistoryRecyclerView(savedHistoryData)
                } else {
                    initInventoryHistoryRecyclerView(savedHistoryData)
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.isNullOrEmpty()) {
                    fetchInventoryHistoryList(p0.toString())
                } else {
                    initInventoryHistoryRecyclerView(savedHistoryData)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (!p0.isNullOrEmpty()) {
                    fetchInventoryHistoryList(p0.toString())
                } else {
                    initInventoryHistoryRecyclerView(savedHistoryData)
                }
            }
        })

        binding.edInventoryDetailSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                fetchInventoryHistoryList(binding.edInventoryDetailSearch.text.toString())
                true // consume the action
            } else {
                false // pass on to other listeners.
            }
        }
    }

    private fun getHotelId() {
        inventoryDetailViewModel.getSelectedHotelData().observe(this) { hotel ->
            hotelId = hotel.id
        }
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
            inventoryDetailViewModel.getDetailInventory(itemId, hotelId)
                .observe(this) { inventory ->
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

                                handleEmptyStates(inventory.data.historyList)

                                savedHistoryData = inventory.data.historyList
                            }
                        }
                    }
                }
        }
    }

    private fun fetchInventoryHistoryList(key: String) {
        val itemId = intent.getStringExtra(INVENTORY_ITEM_ID)

        if (!itemId.isNullOrEmpty()) {
            inventoryDetailViewModel.getInventoryHistoryList(itemId, key)
                .observe(this) { inventoryHistory ->
                    when (inventoryHistory) {
                        is Resource.Error -> {
                            showLoading(false)

                            if (!isInternetAvailable(this)) {
                                showToast(getString(R.string.check_internet))
                            } else {
                                showToast(inventoryHistory.message.toString())
                            }

                            if (inventoryHistory.message == "History not found") {
                                initInventoryHistoryRecyclerView(listOf())
                            }
                        }
                        is Resource.Loading -> {
                            showLoading(true)
                        }
                        is Resource.Message -> {
                            showLoading(false)
                            Timber.tag("InventoryDetailActivity").d(inventoryHistory.message)
                        }
                        is Resource.Success -> {
                            showLoading(false)

                            if (inventoryHistory.data != null) {
                                initInventoryHistoryRecyclerView(inventoryHistory.data)
                            }
                        }
                    }
                }
        }
    }

    private fun handleEmptyStates(historyList: List<InventoryHistory>) {
        binding.tvEmptyInventoryHistory.visibility =
            if (historyList.isEmpty()) View.VISIBLE else View.GONE
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
            intentToChooseItem.putExtra(INVENTORY_ITEM_STOCK, itemStock)

            startActivity(intentToChooseItem)
        }
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.refInventoryDetail.isRefreshing = isLoading
    }

    companion object {
        private const val INVENTORY_ITEM_ID = "inventory_item_id"
        private const val INVENTORY_ITEM_NAME = "inventory_item_name"
        private const val INVENTORY_ITEM_TYPE = "inventory_item_type"
        private const val INVENTORY_ITEM_STOCK = "inventory_item_stock"

        private const val FLAG_UPDATE = "inventory_flag_update"
    }
}