package com.project.acehotel.features.dashboard.management.inventory.add_item

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.utils.DateUtils
import com.project.acehotel.core.utils.constants.inventoryTypeList
import com.project.acehotel.core.utils.constants.mapToInventoryType
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.ActivityAddItemInventoryBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AddItemInventoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddItemInventoryBinding
    private val addItemInventoryViewModel: AddItemInventoryViewModel by viewModels()

    private var stockCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddItemInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        initInventoryType()

        handleButtonBack()

        handleEditText()

        initItemInfo()

        handleStockButton()

        handleSaveButton()
    }

    private fun initInventoryType() {
        val adapter = ArrayAdapter(this, R.layout.drop_inventory_item, inventoryTypeList)

        binding.edAddItemType.apply {
            setAdapter(adapter)
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    binding.layoutAddItemType.isHintEnabled = p0.isNullOrEmpty()
                }
            })
        }
    }

    private fun handleSaveButton() {
        checkForms()

        binding.apply {
            btnSave.setOnClickListener {
                val name = edAddItemName.text.toString()
                val type = mapToInventoryType(edAddItemType.text.toString())

                addItemInventoryViewModel.executeAddInventory(name, type, stockCount)
                    .observe(this@AddItemInventoryActivity) { inventory ->
                        when (inventory) {
                            is Resource.Error -> {
                                showLoading(false)
                                isButtonEnabled(true)

                                if (!isInternetAvailable(this@AddItemInventoryActivity)) {
                                    showToast(getString(R.string.check_internet))
                                } else {
                                    showToast(inventory.message.toString())
                                }
                            }
                            is Resource.Loading -> {
                                showLoading(true)
                                isButtonEnabled(false)
                            }
                            is Resource.Message -> {
                                showLoading(false)
                                isButtonEnabled(true)

                                Timber.tag("AddItemInventoryActivity").d(inventory.message)
                            }
                            is Resource.Success -> {
                                showLoading(false)
                                isButtonEnabled(true)

                                showToast("Barang telah berhasil ditambahkan")
                                finish()
                            }
                        }
                    }
            }
        }
    }

    private fun handleStockButton() {
        binding.apply {
            btnIncreaseStock.setOnClickListener {
                ++stockCount
                tvAddItemStock.text = stockCount.toString()
            }

            btnDecreaseStock.setOnClickListener {
                if (stockCount > 0) {
                    --stockCount
                    tvAddItemStock.text = stockCount.toString()
                }
            }
        }
    }

    private fun initItemInfo() {
        val currentDate = DateUtils.getCurrentDateTime()

        binding.apply {
            edAddItemDate.setText(currentDate)
        }
    }

    private fun handleEditText() {
        binding.apply {
            edAddItemName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0.isNullOrEmpty()) {
                        checkForms()
                        binding.layoutAddItemName.error = getString(R.string.field_cant_empty)
                    } else {
                        binding.layoutAddItemName.error = null
                    }
                }
            })

            edAddItemType.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkForms()
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0.isNullOrEmpty()) {
                        checkForms()
                        binding.layoutAddItemType.error = getString(R.string.field_cant_empty)
                    } else {
                        binding.layoutAddItemType.error = null
                    }
                }
            })
        }
    }

    private fun checkForms() {
        binding.apply {
            val name = edAddItemName.text.toString()
            val type = edAddItemType.text.toString()

            isButtonEnabled(name.isNotEmpty() && type.isNotEmpty())
        }
    }

    private fun handleButtonBack() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }

    private fun isButtonEnabled(isEnabled: Boolean) {
        binding.btnSave.isEnabled = isEnabled
    }

    private fun showLoading(isLoading: Boolean) {
        binding.refAddInventory.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val INVENTORY_ITEM_ID = "inventory_item_id"
        private const val INVENTORY_ITEM_NAME = "inventory_item_name"
        private const val INVENTORY_ITEM_TYPE = "inventory_item_type"
        private const val INVENTORY_ITEM_STOCK = "inventory_item_stock"

        private const val FLAG_UPDATE = "inventory_flag_update"
    }
}