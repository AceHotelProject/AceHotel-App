package com.project.acehotel.core.data.source

import com.project.acehotel.core.data.source.remote.network.ApiResponse
import kotlinx.coroutines.flow.*

abstract class NetworkBoundResource<ResultType, RequestType> {
    private var result: Flow<Resource<ResultType>> = flow {
        emit(Resource.Loading())
        when (val apiResponse = createCall().first()) {
            is ApiResponse.Success -> {
                emit(Resource.Success(fetchFromApi(apiResponse.data)))
            }
            is ApiResponse.Error -> {
                onFetchFailed()
                emit(Resource.Error(apiResponse.errorMessage))
            }
            is ApiResponse.Empty -> {
                emit(Resource.Message("Empty"))
            }
        }
    }

    protected open fun onFetchFailed() {}

    protected abstract suspend fun fetchFromApi(response: RequestType): ResultType

    protected abstract suspend fun createCall(): Flow<ApiResponse<RequestType>>

    fun asFlow(): Flow<Resource<ResultType>> = result

    // FOR OFFLINE FIRST APP
//
//    private var result: Flow<Resource<ResultType>> = flow {
//        emit(Resource.Loading())
//        val dbSource = loadFromDB().first()
//        if (shouldFetch(dbSource)) {
//            emit(Resource.Loading())
//            when (val apiResponse = createCall().first()) {
//                is ApiResponse.Success -> {
//                    saveCallResult(apiResponse.data)
//                    emitAll(loadFromDB().map { Resource.Success(it) })
//                }
//                is ApiResponse.Empty -> {
//                    emitAll(loadFromDB().map { Resource.Success(it) })
//                }
//                is ApiResponse.Error -> {
//                    onFetchFailed()
//                    emit(Resource.Error<ResultType>(apiResponse.errorMessage))
//                }
//            }
//        } else {
//            emitAll(loadFromDB().map { Resource.Success(it) })
//        }
//    }
//
//    protected open fun onFetchFailed() {}
//
//    protected abstract fun loadFromDB(): Flow<ResultType>
//
//    protected abstract fun shouldFetch(data: ResultType?): Boolean
//
//    protected abstract suspend fun createCall(): Flow<ApiResponse<RequestType>>
//
//    protected abstract suspend fun saveCallResult(data: RequestType)
//
//    fun asFlow(): Flow<Resource<ResultType>> = result
//
// FOR OFFLINE FIRST APP
}