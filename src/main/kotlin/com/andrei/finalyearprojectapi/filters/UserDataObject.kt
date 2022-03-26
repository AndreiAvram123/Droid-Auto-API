package com.andrei.finalyearprojectapi.filters

import com.andrei.finalyearprojectapi.entity.User


open class FilterDataObject {
    var user: User? = null

    fun getUserNotNull(): User {
        val temp = user
        check(temp != null) {
            "Security concern. When the userID is used down the pipeline it should never be null"
        }
        return temp
    }
}

