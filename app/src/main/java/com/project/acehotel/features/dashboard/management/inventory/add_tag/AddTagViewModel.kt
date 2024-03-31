package com.project.acehotel.features.dashboard.management.inventory.add_tag

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.inventory.model.Tag
import com.project.acehotel.core.domain.inventory.usecase.InventoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddTagViewModel @Inject constructor(
    private val inventoryUseCase: InventoryUseCase
) : ViewModel() {

    private fun getTagById(readerId: String) = inventoryUseCase.getTagById(readerId).asLiveData()

    private fun addTag(
        tid: String,
        inventoryId: String
    ) = inventoryUseCase.addTag(tid, inventoryId).asLiveData()

    fun executeAddTag(
        readerId: String,
        inventoryId: String
    ): MediatorLiveData<Resource<Tag>> = MediatorLiveData<Resource<Tag>>().apply {
        addSource(getTagById(readerId)) { tagId ->
            when (tagId) {
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
                is Resource.Message -> {

                }
                is Resource.Success -> {
                    addSource(addTag(tagId.data ?: "", inventoryId)) { tag ->
                        value = tag
                    }
                }
            }
        }
    }
}