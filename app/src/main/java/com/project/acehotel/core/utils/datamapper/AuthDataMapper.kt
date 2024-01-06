package com.project.acehotel.core.utils.datamapper

import com.project.acehotel.core.data.source.local.entity.TokenData
import com.project.acehotel.core.data.source.local.entity.TokenEntity
import com.project.acehotel.core.data.source.local.entity.UserEntity
import com.project.acehotel.core.data.source.remote.response.auth.AuthResponse
import com.project.acehotel.core.domain.auth.model.Auth
import com.project.acehotel.core.domain.auth.model.Tokens
import com.project.acehotel.core.domain.auth.model.TokensFormat
import com.project.acehotel.core.domain.auth.model.User
import com.project.acehotel.core.utils.constants.UserRole

object AuthDataMapper {

    fun mapAuthResponseToDomain(input: AuthResponse): Auth = Auth(
        id = 1,
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
                token = input.tokens?.refresh?.token ?: "Empty",
                expires = input.tokens?.refresh?.expires ?: "Empty"
            ),
        )
    )

    fun mapAuthToEntity(input: Auth): UserEntity = UserEntity(
        userId = input.user?.id ?: "",
        role = input.user?.role,
        username = input.user?.username,
        email = input.user?.email,
        tokenInfo = TokenEntity(
            accessToken = input.tokens?.accessToken?.token,
            accessTokenExpire = input.tokens?.accessToken?.expires,
            refreshToken = input.tokens?.refreshToken?.token,
            refreshTokenExpire = input.tokens?.refreshToken?.expires,
        )
    )

    fun mapTokenDataToDomain(input: TokenData): Tokens = Tokens(
        accessToken = TokensFormat(
            token = input.accessToken ?: "Empty",
            expires = input.accessTokenExpire ?: "Empty",
        ),
        refreshToken = TokensFormat(
            token = input.refreshToken ?: "Empty",
            expires = input.refreshTokenExpire ?: "Empty",
        )
    )

    fun mapUserEntityToDomain(input: UserEntity?): Auth = Auth(
        id = input?.id ?: 1,
        user = User(
            role = input?.role ?: UserRole.UNDEFINED,
            username = input?.username ?: "Empty",
            email = input?.email ?: "Empty",
            id = input?.userId ?: "Empty"
        ),
        tokens = Tokens(
            accessToken = TokensFormat(
                token = input?.tokenInfo?.accessToken ?: "Empty",
                expires = input?.tokenInfo?.accessTokenExpire ?: "Empty",
            ),
            refreshToken = TokensFormat(
                token = input?.tokenInfo?.refreshToken ?: "Empty",
                expires = input?.tokenInfo?.refreshTokenExpire ?: "Empty",
            )
        )
    )
}