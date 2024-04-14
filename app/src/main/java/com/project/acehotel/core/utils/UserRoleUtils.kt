package com.project.acehotel.core.utils

import com.project.acehotel.core.utils.constants.UserRole

fun mapUserRole(type: String): UserRole {
    return when (type) {
        UserRole.MASTER.role -> {
            UserRole.MASTER
        }
        UserRole.FRANCHISE.role -> {
            UserRole.FRANCHISE
        }
        UserRole.INVENTORY_STAFF.role -> {
            UserRole.INVENTORY_STAFF
        }
        UserRole.RECEPTIONIST.role -> {
            UserRole.RECEPTIONIST
        }
        UserRole.ADMIN.role -> {
            UserRole.ADMIN
        }
        UserRole.UNDEFINED.role -> {
            UserRole.UNDEFINED
        }
        else -> {
            UserRole.UNDEFINED
        }
    }
}