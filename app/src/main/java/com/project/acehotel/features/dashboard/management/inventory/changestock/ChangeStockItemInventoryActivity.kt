package com.project.acehotel.features.dashboard.management.inventory.changestock

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.project.acehotel.databinding.ActivityChangeStockItemInventoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeStockItemInventoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeStockItemInventoryBinding
    private val changeStockItemViewModel: ChangeStockItemInventoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChangeStockItemInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
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