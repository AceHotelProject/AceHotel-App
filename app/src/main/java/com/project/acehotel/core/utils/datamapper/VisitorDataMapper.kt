package com.project.acehotel.core.utils.datamapper

import com.project.acehotel.core.data.source.remote.response.visitor.ListVisitorResponse
import com.project.acehotel.core.data.source.remote.response.visitor.VisitorResponse
import com.project.acehotel.core.domain.visitor.model.Visitor

object VisitorDataMapper {

    fun mapVisitorResponseToDomain(input: VisitorResponse): Visitor = Visitor(
        id = input.id ?: "",
        name = input.name ?: "",
        address = input.address ?: "",
        identity_num = input.identityNum ?: "",
        phone = input.phone ?: "",
        email = input.email ?: "",
        identityImage = input.pathIdentityImage ?: "",
    )

    fun mapVisitorListResponseToDomain(input: ListVisitorResponse): List<Visitor> =
        input.results?.map { visitor ->
            Visitor(
                id = visitor?.id ?: "",
                name = visitor?.name ?: "",
                address = visitor?.address ?: "",
                identity_num = visitor?.identityNum ?: "",
                phone = visitor?.phone ?: "",
                email = visitor?.email ?: "",
                identityImage = visitor?.pathIdentityImage ?: "",
            )
        } ?: listOf()
}