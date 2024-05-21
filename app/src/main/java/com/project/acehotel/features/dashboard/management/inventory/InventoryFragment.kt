package com.project.acehotel.features.dashboard.management.inventory

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.project.acehotel.R
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.data.source.remote.MQTTService
import com.project.acehotel.core.domain.inventory.model.Inventory
import com.project.acehotel.core.domain.inventory.model.Reader
import com.project.acehotel.core.ui.adapter.inventory.InventoryListAdapter
import com.project.acehotel.core.utils.DateUtils
import com.project.acehotel.core.utils.constants.InventoryType
import com.project.acehotel.core.utils.constants.MQTT_TOPIC
import com.project.acehotel.core.utils.isInternetAvailable
import com.project.acehotel.core.utils.showLongToast
import com.project.acehotel.core.utils.showToast
import com.project.acehotel.databinding.FragmentInventoryBinding
import com.project.acehotel.features.dashboard.management.IManagementSearch
import com.project.acehotel.features.dashboard.management.inventory.add_item.AddItemInventoryActivity
import com.project.acehotel.features.dashboard.management.inventory.choose_item.ChooseItemInventoryActivity
import com.project.acehotel.features.dashboard.management.inventory.detail.InventoryDetailActivity
import com.project.acehotel.features.dashboard.management.inventory.edit_reader.EditReaderActivity
import dagger.hilt.android.AndroidEntryPoint
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class InventoryFragment : Fragment(), IManagementSearch {
    private var _binding: FragmentInventoryBinding? = null
    private val binding get() = _binding!!

    private val inventoryViewModel: InventoryViewModel by activityViewModels()

    private var readerData: Reader? = null

    @Inject
    lateinit var mqttClient: MQTTService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // un-comment this to use MQTT
        // initMQTT()

        fetchReaderData()

        handleEmptyStates(listOf())

        fetchInventoryItems("")

        handleRefreshLayout("")

        handleButtonAddInventory()

        handleButtonEditReader()

        handleButtonAddTag()
    }

    private fun fetchReaderData() {
        inventoryViewModel.getReader(READER_NAME).observe(requireActivity()) { reader ->
            when (reader) {
                is Resource.Error -> {
                    showLoading(false)

                    if (!isInternetAvailable(requireContext())) {
                        activity?.showToast(getString(R.string.check_internet))
                    } else {
                        activity?.showToast(reader.message.toString())
                    }
                }

                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Message -> {
                    showLoading(false)
                    Timber.tag("InventoryFragment").d(reader.message)
                }

                is Resource.Success -> {
                    binding.apply {
                        readerData = reader.data

                        tvInventoryReaderStatus.text = "Aktif"
                        tvInventoryPowerGain.text = "${reader.data?.powerGain.toString()} dB"
                        tvInventoryReadInterval.text = "${reader.data?.readInterval.toString()} ms"
                        tvInventoryDate.text = DateUtils.getDateThisDay2()
                    }
                }
            }
        }
    }

    private fun handleEmptyStates(inventory: List<Inventory>?) {
        binding.apply {
            rvListInventory.layoutParams.width =
                if (inventory?.isEmpty()!!) 400 else ViewGroup.LayoutParams.MATCH_PARENT
            rvListInventory.layoutParams.height =
                if (inventory?.isEmpty()!!) 400 else ViewGroup.LayoutParams.WRAP_CONTENT
            tvEmptyInventoryNext.visibility =
                if (inventory?.isEmpty()!!) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun initMQTT() {
        if (!mqttClient.isConnected()) {
            mqttClient.connect(
                object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        Timber.tag("MQTT").d("Success connected to MQTT")

                        mqttClient.subscribe(
                            MQTT_TOPIC,
                            1,
                            object : IMqttActionListener {
                                override fun onSuccess(asyncActionToken: IMqttToken?) {
                                    Timber.tag("MQTT").d("Success subscribed to ACE HOTEL Topic")
                                }

                                override fun onFailure(
                                    asyncActionToken: IMqttToken?,
                                    exception: Throwable?
                                ) {
                                    Timber.tag("MQTT").e(exception)
                                }
                            }
                        )
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Timber.tag("MQTT").e(exception)
                    }
                },
                object : MqttCallback {
                    override fun connectionLost(cause: Throwable?) {
                        Timber.tag("MQTT").e("Connection lost")
                    }

                    override fun messageArrived(topic: String?, message: MqttMessage?) {
                        val msg = "Receive message: ${message.toString()} from topic: $topic"

                        Timber.tag("RESULT").e(message.toString())
                        // TODO
                        // implement in layout
                        activity?.showToast(msg)
                    }

                    override fun deliveryComplete(token: IMqttDeliveryToken?) {
                        Timber.tag("MQTT").d("Delivery complete")
                    }
                }
            )
        }
    }

    private fun handleButtonAddTag() {
        binding.btnAddTag.setOnClickListener {

            inventoryViewModel.getTagById(READER_NAME).observe(requireActivity()) { tag ->
                when (tag) {
                    is Resource.Error -> {
                        activity?.showLongToast("Tag tidak terdeteksi")
                    }

                    is Resource.Loading -> {
                        activity?.showLongToast("Reader sedang mendeteksi tag")
                    }

                    is Resource.Message -> {
                        activity?.showLongToast("Tag tidak terdeteksi")
                    }

                    is Resource.Success -> {
                        if (tag.data != null) {
                            activity?.showLongToast("Tag berhasil terdeteksi")

                            val intentToChooseItemInventory =
                                Intent(requireContext(), ChooseItemInventoryActivity::class.java)
                            intentToChooseItemInventory.putExtra(IS_ADD_TAG, true)
                            intentToChooseItemInventory.putExtra(TAG_DATA, tag.data)
                            startActivity(intentToChooseItemInventory)
                        }
                    }
                }
            }
        }
    }

    private fun handleButtonEditReader() {
        binding.apply {
            btnEditReader.setOnClickListener {
                val intentToEditReader = Intent(requireContext(), EditReaderActivity::class.java)
                val jsonData = Gson().toJson(readerData, Reader::class.java)
                intentToEditReader.putExtra(READER_DATA, jsonData)
                startActivity(intentToEditReader)
            }
        }
    }

    private fun handleButtonAddInventory() {
        binding.btnAddInventory.setOnClickListener {
            val intentToAddInventory =
                Intent(requireContext(), AddItemInventoryActivity::class.java)
            startActivity(intentToAddInventory)
        }
    }

    private fun handleRefreshLayout(filter: String) {
        binding.apply {
            svInventory.viewTreeObserver.addOnScrollChangedListener {
                refInventory.isEnabled = svInventory.scrollY == 0
            }

            refInventory.setOnRefreshListener {
                fetchInventoryItems(filter)

                fetchReaderData()
            }
        }
    }

    private fun fetchInventoryItems(query: String) {
        var type = ""
        var name = ""

        if (query == "linen" || query == "Linen") {
            type = "linen"
        } else if (query == "Kasur" || query == "kasur") {
            type = "kasur"
        } else {
            name = query
        }

        inventoryViewModel.fetchListInventory(name, type).observe(requireActivity()) { item ->
            when (item) {
                is Resource.Error -> {
                    showLoading(false)

                    if (!isInternetAvailable(requireContext())) {
                        activity?.showToast(getString(R.string.check_internet))
                    } else {
                        activity?.showToast(item.message.toString())
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

                    handleEmptyStates(item.data)

                    initInventoryRecyclerView(item.data)

                    initQuickInfo(item.data)
                }
            }
        }
    }

    private fun initQuickInfo(data: List<Inventory>?) {
        var linenTotal = 0
        var bedTotal = 0
        var foodTotal = 0
        var drinkTotal = 0

        if (data != null) {
            for (i in data.indices) {
                when (data[i].type) {
                    InventoryType.BED.type -> {
                        ++bedTotal
                    }

                    InventoryType.LINEN.type -> {
                        ++linenTotal
                    }

                    InventoryType.FOOD.type -> {
                        ++foodTotal
                    }

                    InventoryType.DRINK.type -> {
                        ++drinkTotal
                    }
                }
            }
        }

        binding.apply {
            tvTotalLinen.text = linenTotal.toString()
            tvTotalBed.text = bedTotal.toString()

            tvTotalFood.text = foodTotal.toString()
            tvTotalDrink.text = drinkTotal.toString()
        }
    }

    private fun initInventoryRecyclerView(listInventory: List<Inventory>?) {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvListInventory.layoutManager = layoutManager

        val adapter = InventoryListAdapter(listInventory)
        binding.rvListInventory.adapter = adapter

        adapter.setOnItemClickCallback(object : InventoryListAdapter.OnItemClickCallback {
            override fun onItemClicked(
                context: Context,
                id: String,
                name: String,
                type: String,
                stock: Int
            ) {
                val intentToInventoryDetail =
                    Intent(requireContext(), InventoryDetailActivity::class.java)

                intentToInventoryDetail.putExtra(INVENTORY_ITEM_ID, id)
                intentToInventoryDetail.putExtra(INVENTORY_ITEM_NAME, name)
                intentToInventoryDetail.putExtra(INVENTORY_ITEM_TYPE, type)

                startActivity(intentToInventoryDetail)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInventoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun showLoading(isLoading: Boolean) {
        binding.refInventory.isRefreshing = isLoading
    }

    override fun onSearchQuery(query: String) {
        fetchInventoryItems(query)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    companion object {
        private const val INVENTORY_ITEM_ID = "inventory_item_id"
        private const val INVENTORY_ITEM_NAME = "inventory_item_name"

        private const val INVENTORY_ITEM_TYPE = "inventory_item_type"

        private const val READER_DATA = "reader_data"
        private const val IS_ADD_TAG = "is_add_tag"
        private const val TAG_DATA = "tag_data"

        private const val READER_NAME = "ACE-001"
    }
}