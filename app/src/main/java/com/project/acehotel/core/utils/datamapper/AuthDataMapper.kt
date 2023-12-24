package com.project.acehotel.core.utils.datamapper

import com.project.acehotel.core.data.source.remote.response.auth.AuthResponse
import com.project.acehotel.core.domain.auth.model.Auth
import com.project.acehotel.core.domain.auth.model.Tokens
import com.project.acehotel.core.domain.auth.model.TokensFormat
import com.project.acehotel.core.domain.auth.model.User
import com.project.acehotel.core.utils.constants.UserRole

object AuthDataMapper {

    fun mapAuthResponseToDomain(input: AuthResponse): Auth = Auth(
        user = User(
            role = when (input.user?.role) {
                UserRole.MASTER.role -> {
                    UserRole.MASTER
                }
                UserRole.FRANCHISE.role -> {
                    UserRole.FRANCHISE
                }
                UserRole.EMPLOYEE.role -> {
                    UserRole.EMPLOYEE
                }
                UserRole.ADMIN.role -> {
                    UserRole.ADMIN
                }
                else -> UserRole.EMPLOYEE
            },
            username = input.user?.username ?: "Empty",
            email = input.user?.email ?: "Empty",
            id = input.user?.id ?: "Empty"
        ),
        tokens = Tokens(
            accessToken = TokensFormat(
                input.tokens?.access?.token ?: "Empty",
                expires = input.tokens?.access?.expires ?: "Empty"
            ),
            refreshToken = TokensFormat(
                input.tokens?.access?.token ?: "Empty",
                expires = input.tokens?.access?.expires ?: "Empty"
            ),
        )
    )
}