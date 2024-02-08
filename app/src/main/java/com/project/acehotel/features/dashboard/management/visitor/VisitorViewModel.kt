package com.project.acehotel.features.dashboard.management.visitor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.acehotel.core.domain.visitor.usecase.VisitorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VisitorViewModel @Inject constructor(private val visitorUseCase: VisitorUseCase) :
    ViewModel() {

    fun getVisitorList() = visitorUseCase.getVisitorList().asLiveData()
}