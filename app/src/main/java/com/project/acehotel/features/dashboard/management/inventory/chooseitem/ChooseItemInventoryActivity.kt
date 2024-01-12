package com.project.acehotel.features.dashboard.management.inventory.chooseitem

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
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

        fetchInventoryItems()

        handleOnRefresh()

        handleButtonAddItems()
    }

    private fun handleButtonAddItems() {
        binding.btnAddItem.setOnClickListener {
            val intentToAddItem = Intent(this, AddItemInventoryActivity::class.java)
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
            override fun onItemClicked(context: Context) {
                val intentToAddItem =
                    Intent(this@ChooseItemInventoryActivity, AddItemInventoryActivity::class.java)
                startActivity(intentToAddItem)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.refInventoryItem.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }
}