package com.project.acehotel.features.dashboard.management.inventory.detail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.inventory.model.Inventory
import com.project.acehotel.core.domain.inventory.model.InventoryHistory
import com.project.acehotel.core.ui.adapter.inventory.InventoryHistoryAdapter
import com.project.acehotel.core.utils.DateUtils
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.ActivityInventoryDetailBinding
import com.project.acehotel.features.dashboard.management.inventory.chooseitem.ChooseItemInventoryActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class InventoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInventoryDetailBinding
    private val inventoryDetailViewModel: InventoryDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInventoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        handleFab()

        setupBackButton()

        fetchInventoryDetail()
    }

    private fun fetchInventoryDetail() {
        val itemId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(INVENTORY_ITEM_ID, Inventory::class.java)?.id
        } else {
            intent.getParcelableExtra<Inventory>(INVENTORY_ITEM_ID)?.id
        }

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
            }
        }
    }

    private fun setupBackButton() {
        binding.btnInventoryDetailBack.setOnClickListener {
            finish()
        }
    }

    private fun handleFab() {
        binding.fabInventoryDetailChangeStock.setOnClickListener {
            val intentToChooseItem = Intent(this, ChooseItemInventoryActivity::class.java)
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
    }
}