package com.andrei.finalyearprojectapi.filters

import com.andrei.finalyearprojectapi.entity.User
import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope

@Component
@RequestScope
class RequestDataObject {
    var user: User? = null

    fun getUserNotNull(): User {
        val temp = user
        check(temp != null) {
            "Security concern. When the userID is used down the pipeline it should never be null"
        }
        return temp
    }
}