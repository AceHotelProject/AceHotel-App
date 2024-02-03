package com.project.acehotel.core.utils.constants

enum class UserRole(val role: String, val display: String) {
    MASTER("owner", "Master"),
    FRANCHISE("branch_manager", "Franchise"),
    CLEANING_STAFF("cleaning_staff", "Cleaning"),
    INVENTORY_STAFF("inventory_staff", "Inventory"),
    RECEPTIONIST("receptionist", "Receptionist"),
    ADMIN("admin", "Admin"),
    UNDEFINED("role", "Role")
}