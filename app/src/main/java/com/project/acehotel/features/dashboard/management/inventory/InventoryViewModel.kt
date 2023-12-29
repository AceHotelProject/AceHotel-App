package com.project.acehotel.features.dashboard.management.inventory

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.auth.usecase.AuthUseCase
import com.project.acehotel.core.domain.inventory.model.Inventory
import com.project.acehotel.core.domain.inventory.usecase.InventoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val inventoryUseCase: InventoryUseCase,
    private val authUseCase: AuthUseCase,
) : ViewModel() {

    fun getUser() = authUseCase.getUser().asLiveData()

    fun getListInventory(token: String) = inventoryUseCase.getListInventory(token).asLiveData()

    fun fetchListInventory(): MediatorLiveData<Resource<List<Inventory>>> =
        MediatorLiveData<Resource<List<Inventory>>>().apply {
            addSource(getUser()) { user ->
                addSource(getListInventory(("Bearer " + user.tokens?.accessToken?.token) ?: "")) {
                    value = it
                }
            }
        }

    fun getTokens() = authUseCase.getTokens().asLiveData()
}