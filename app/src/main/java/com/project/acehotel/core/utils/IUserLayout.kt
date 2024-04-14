package com.project.acehotel.core.utils

import com.project.acehotel.core.utils.constants.UserRole

interface IUserLayout {
    fun changeLayoutByUser(userRole: UserRole)
}