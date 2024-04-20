package com.project.acehotel.core.utils.datamapper

import com.project.acehotel.core.data.source.local.entity.TokenEntity
import com.project.acehotel.core.data.source.local.entity.UserEntity
import com.project.acehotel.core.data.source.remote.response.auth.AuthResponse
import com.project.acehotel.core.data.source.remote.response.user.ListUserResponse
import com.project.acehotel.core.data.source.remote.response.user.UserResponse
import com.project.acehotel.core.domain.auth.model.Auth
import com.project.acehotel.core.domain.auth.model.Tokens
import com.project.acehotel.core.domain.auth.model.TokensFormat
import com.project.acehotel.core.domain.auth.model.User
import com.project.acehotel.core.utils.constants.UserRole
import com.project.acehotel.core.utils.constants.mapToUserRole
import org.json.JSONArray

object AuthDataMapper {

    fun mapAuthResponseToDomain(input: AuthResponse): Auth = Auth(
        id = 1,
        user = User(
            role = input.user?.role?.let { mapToUserRole(it) },
            username = input.user?.username ?: "Empty",
            email = input.user?.email ?: "Empty",
            id = input.user?.id ?: "Empty",
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

    fun mapUserEntityToDomain(input: UserEntity?): Auth = Auth(
        id = input?.id ?: 1,
        user = User(
            role = input?.role ?: UserRole.UNDEFINED,
            username = input?.username ?: "Empty",
            email = input?.email ?: "Empty",
            id = input?.userId ?: "Empty",
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

    fun mapUserResponseToDomain(input: UserResponse): User = User(
        id = input.id ?: "Empty",
        username = input.username ?: "Empty",
        email = input.email ?: "Empty",
        role = mapToUserRole(input.role ?: "role"),
    )

    fun mapListUserResponseToDomain(input: ListUserResponse): List<User> =
        input.results?.map { user ->
            User(
                id = user?.id ?: "Empty",
                username = user?.username ?: "Empty",
                email = user?.email ?: "Empty",
                role = mapToUserRole(user?.role ?: "role"),
            )
        } ?: listOf()

    private fun convertStringToList(input: String): List<String> {
        val jsonArray = JSONArray(input)

        val outputList = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            outputList.add(jsonArray.getString(i))
        }

        return outputList
    }
}