package com.project.acehotel.core.domain.visitor.interactor

import com.project.acehotel.core.data.repository.VisitorRepository
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.visitor.model.Visitor
import com.project.acehotel.core.domain.visitor.usecase.VisitorUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VisitorInteractor @Inject constructor(private val visitorRepository: VisitorRepository) :
    VisitorUseCase {
    override fun getVisitorList(
        hotelId: String,
        name: String,
        email: String,
        identityNum: String
    ): Flow<Resource<List<Visitor>>> {
        return visitorRepository.getVisitorList(hotelId, name, email, identityNum)
    }

    override fun getVisitorDetail(id: String): Flow<Resource<Visitor>> {
        return visitorRepository.getVisitorDetail(id)
    }

}