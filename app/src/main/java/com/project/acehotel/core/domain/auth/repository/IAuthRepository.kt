package com.project.acehotel.core.domain.auth.repository

import com.project.acehotel.core.data.source.Resource
import com.project.acehotel.core.domain.auth.model.Auth
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {

    fun loginUser(email: String, password: String) : Flow<Resource<Auth>>

}