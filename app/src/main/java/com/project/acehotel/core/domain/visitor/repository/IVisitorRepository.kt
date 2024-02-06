package com.project.acehotel.core.domain.visitor.repository

import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.visitor.model.Visitor
import kotlinx.coroutines.flow.Flow

interface IVisitorRepository {
    fun getVisitorList(): Flow<Resource<List<Visitor>>>

    fun getVisitorDetail(id: String): Flow<Resource<Visitor>>
}