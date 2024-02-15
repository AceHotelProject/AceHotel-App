package com.project.acehotel.core.domain.visitor.usecase

import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.visitor.model.Visitor
import kotlinx.coroutines.flow.Flow

interface VisitorUseCase {

    fun getVisitorList(
        hotelId: String,
        name: String,
        email: String,
        identityNum: String
    ): Flow<Resource<List<Visitor>>>

    fun getVisitorDetail(id: String): Flow<Resource<Visitor>>
}