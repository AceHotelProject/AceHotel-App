package com.project.acehotel.core.domain.visitor.repository

import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.visitor.model.Visitor
import kotlinx.coroutines.flow.Flow

interface IVisitorRepository {
    fun getVisitorList(
        hotelId: String,
        name: String,
        email: String,
        identityNum: String
    ): Flow<Resource<List<Visitor>>>

    fun getVisitorDetail(id: String): Flow<Resource<Visitor>>

    fun addVisitor(
        hotelId: String,
        name: String,
        address: String,
        phone: String,
        email: String,
        identityNum: String,
        pathIdentityImage: String,
    ): Flow<Resource<Visitor>>

    fun updateVisitor(
        id: String,
        hotelId: String,
        name: String,
        address: String,
        phone: String,
        email: String,
        identityNum: String,
        pathIdentityImage: String,
    ): Flow<Resource<Visitor>>

    fun deleteVisitor(
        id: String,
        hotelId: String,
    ): Flow<Resource<Int>>
}