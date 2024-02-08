package com.project.acehotel.core.data.repository

import com.project.acehotel.core.data.source.NetworkBoundResource
import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.data.source.local.LocalDataSource
import com.project.acehotel.core.data.source.remote.RemoteDataSource
import com.project.acehotel.core.data.source.remote.network.ApiResponse
import com.project.acehotel.core.data.source.remote.response.visitor.ListVisitorResponse
import com.project.acehotel.core.data.source.remote.response.visitor.VisitorResponse
import com.project.acehotel.core.domain.visitor.model.Visitor
import com.project.acehotel.core.domain.visitor.repository.IVisitorRepository
import com.project.acehotel.core.utils.AppExecutors
import com.project.acehotel.core.utils.datamapper.VisitorDataMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VisitorRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IVisitorRepository {
    override fun getVisitorList(): Flow<Resource<List<Visitor>>> {
        return object : NetworkBoundResource<List<Visitor>, ListVisitorResponse>() {
            override suspend fun fetchFromApi(response: ListVisitorResponse): List<Visitor> {
                return VisitorDataMapper.mapVisitorListResponseToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<ListVisitorResponse>> {
                return remoteDataSource.getListVisitor()
            }
        }.asFlow()
    }

    override fun getVisitorDetail(id: String): Flow<Resource<Visitor>> {
        return object : NetworkBoundResource<Visitor, VisitorResponse>() {
            override suspend fun fetchFromApi(response: VisitorResponse): Visitor {
                return VisitorDataMapper.mapVisitorResponseToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<VisitorResponse>> {
                return remoteDataSource.getDetailVisitor(id)
            }
        }.asFlow()
    }

}