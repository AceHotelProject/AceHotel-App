package com.project.acehotel.core.utils.constants

enum class UserRole(val role: String, val display: String) {
    MASTER("owner", "Master"),
    FRANCHISE("branch_manager", "Franchise"),

    //    CLEANING_STAFF("cleaning_staff", "Cleaning"),
    INVENTORY_STAFF("inventory_staff", "Inventory"),
    RECEPTIONIST("receptionist", "Receptionist"),
    ADMIN("admin", "Admin"),
    UNDEFINED("role", "Role")
}

fun mapToUserRole(role: String): UserRole = when (role) {
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
    else -> {
        UserRole.UNDEFINED
    }
}

fun mapToUserDisplay(role: String): String = when (role) {
    UserRole.MASTER.role -> {
        UserRole.MASTER.display
    }
    UserRole.FRANCHISE.role -> {
        UserRole.FRANCHISE.display
    }
    UserRole.INVENTORY_STAFF.role -> {
        UserRole.INVENTORY_STAFF.display
    }
    UserRole.RECEPTIONIST.role -> {
        UserRole.RECEPTIONIST.display
    }
    UserRole.ADMIN.role -> {
        UserRole.ADMIN.display
    }
    else -> {
        UserRole.UNDEFINED.display
    }
}

