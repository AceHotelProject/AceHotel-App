package com.project.acehotel.core.utils.datamapper

import com.project.acehotel.core.data.source.local.entity.UserEntity
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
                token = input.tokens?.access?.token ?: "Empty",
                expires = input.tokens?.access?.expires ?: "Empty"
            ),
            refreshToken = TokensFormat(
                token = input.tokens?.access?.token ?: "Empty",
                expires = input.tokens?.access?.expires ?: "Empty"
            ),
        )
    )

    fun mapAuthToEntity(input: Auth): UserEntity = UserEntity(
        userId = input.user.id,
        role = input.user.role,
        username = input.user.username,
        email = input.user.email,
        accessToken = input.tokens.accessToken.token,
        accessTokenExpire = input.tokens.accessToken.expires,
        refreshToken = input.tokens.refreshToken.token,
        refreshTokenExpire = input.tokens.refreshToken.expires,
    )

    fun mapUserEntityToDomain(input: UserEntity): Auth = Auth(
        user = User(
            role = input.role,
            username = input.username,
            email = input.email,
            id = input.userId
        ),
        tokens = Tokens(
            accessToken = TokensFormat(
                token = input.accessToken,
                expires = input.accessTokenExpire,
            ),
            refreshToken = TokensFormat(
                token = input.refreshToken,
                expires = input.refreshTokenExpire,
            )
        )
    )
}