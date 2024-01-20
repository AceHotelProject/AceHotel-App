package com.project.acehotel.features.dashboard.management.inventory.changestock

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.utils.DateUtils
import com.project.acehotel.core.utils.constants.mapToInventoryDisplay
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.ActivityChangeStockItemInventoryBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ChangeStockItemInventoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeStockItemInventoryBinding
    private val changeStockItemViewModel: ChangeStockItemInventoryViewModel by viewModels()

    private var stockCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChangeStockItemInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        disableRefresh()

        handleButtonBack()

        handleStockButton()

        initItemInfo()

        handleEditText()

        checkForms()

        handleButtonSave()
    }

    private fun handleButtonSave() {
        binding.apply {
            btnSave.setOnClickListener {
                val id = intent.getStringExtra(INVENTORY_ITEM_ID)!!
                val name = edChangeStockItemName.text.toString()
                val type = edChangeStockItemType.text.toString()
                val stock = stockCount
                val title = edChangeStockItemTitle.text.toString()
                val desc = edChangeStockItemDesc.text.toString()

                changeStockItemViewModel.updateInventory(id, name, type, stock, title, desc)
                    .observe(this@ChangeStockItemInventoryActivity) { inventory ->
                        when (inventory) {
                            is Resource.Error -> {
                                showLoading(false)
                                if (!isInternetAvailable(this@ChangeStockItemInventoryActivity)) {
                                    showToast(getString(R.string.check_internet))
                                } else {
                                    Timber.tag("ChangeStockItemInventoryActivity")
                                        .e(inventory.message)
                                }
                            }
                            is Resource.Loading -> {
                                showLoading(true)
                            }
                            is Resource.Message -> {
                                showLoading(false)
                                Timber.tag("ChangeStockItemInventoryActivity").d(inventory.message)
                            }
                            is Resource.Success -> {
                                showLoading(false)
                                showToast("Stok barang berhasil diperbaharui")

                                finish()
                            }
                        }
                    }
            }
        }
    }

    private fun handleEditText() {
        binding.edChangeStockItemTitle.addTextChangedListener {
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }
            }
        }

        binding.edChangeStockItemDesc.addTextChangedListener {
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    checkForms()
                }
            }
        }
    }

    private fun handleStockButton() {
        binding.apply {
            btnIncreaseStock.setOnClickListener {
                ++stockCount
                tvChangeStock.text = stockCount.toString()

                checkForms()
            }

            btnDecreaseStock.setOnClickListener {
                if (stockCount > 0) {
                    --stockCount
                    tvChangeStock.text = stockCount.toString()

                    checkForms()
                }
            }
        }
    }

    private fun checkForms() {
        isButtonEnabled(stockCount > 0 && !binding.edChangeStockItemDesc.text.isNullOrEmpty() && !binding.edChangeStockItemTitle.text.isNullOrEmpty())
    }

    private fun initItemInfo() {
        val currentDate = DateUtils.getCurrentDateTime()
        val itemName = intent.getStringExtra(INVENTORY_ITEM_NAME)
        val itemType = mapToInventoryDisplay(intent.getStringExtra(INVENTORY_ITEM_TYPE).toString())
        val itemStock = intent.getIntExtra(INVENTORY_ITEM_STOCK, 0)

        binding.apply {
            edChangeStockItemName.setText(itemName)
            edChangeStockItemType.setText(itemType)
            edChangeStockItemDate.setText(currentDate)

            stockCount = itemStock
            tvChangeStock.text = (itemStock.toString())
        }
    }

    private fun handleButtonBack() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun isButtonEnabled(isEnabled: Boolean) {
        binding.btnSave.isEnabled = isEnabled
    }

    private fun showLoading(isLoading: Boolean) {
        binding.refChangeStock.isRefreshing = isLoading
    }

    private fun disableRefresh() {
        binding.refChangeStock.isEnabled = false
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