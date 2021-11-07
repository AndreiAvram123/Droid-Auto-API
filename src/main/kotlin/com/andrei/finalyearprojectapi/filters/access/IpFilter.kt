package com.andrei.finalyearprojectapi.filters.access

import com.andrei.finalyearprojectapi.filters.SecurityFilter
import com.andrei.finalyearprojectapi.filters.UserDataObject
import com.andrei.finalyearprojectapi.utils.ResponseWrapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Component
class IpFilter() : SecurityFilter {


    @Autowired
    lateinit var userDataObject: UserDataObject

    override fun shouldCheckRequest(request: HttpServletRequest): Boolean  = true


    override fun isFilterPassed(request: HttpServletRequest): Boolean {
        val allowedIps = userDataObject.user?.ipAddresses
        return true
    }

    override fun generateErrorResponse(): ResponseWrapper<String> {
        TODO("Not yet implemented")
    }
}