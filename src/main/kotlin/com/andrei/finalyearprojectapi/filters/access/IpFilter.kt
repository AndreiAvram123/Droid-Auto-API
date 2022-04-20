package com.andrei.finalyearprojectapi.filters.access

import com.andrei.finalyearprojectapi.configuration.annotations.NoAuthenticationRequired
import com.andrei.finalyearprojectapi.filters.FilterDataObject
import com.andrei.finalyearprojectapi.filters.SecurityFilter
import com.andrei.finalyearprojectapi.utils.ApiResponse
import com.andrei.finalyearprojectapi.utils.endpointHasAnnotation
import com.andrei.finalyearprojectapi.utils.newLoginDevice
import com.example.demo.configuration.getForwardedHeader
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Component
class IpFilter(
    private val filterDataObject: FilterDataObject
) : SecurityFilter {



    override fun shouldCheckRequest(request: HttpServletRequest): Boolean {
        return !request.endpointHasAnnotation<NoAuthenticationRequired>()
    }


    override fun isFilterPassed(request: HttpServletRequest): Boolean {
        //todo
        //might not work
        val allowedIps = filterDataObject.getUserNotNull().ipAddresses ?: return false
        val requestIP = request.getForwardedHeader() ?: request.remoteAddr
        allowedIps.find { it.value == requestIP }?.let {
            return true
        }
        return false
    }

    override fun generateErrorResponse(): ApiResponse<String> {
          return newLoginDevice()
    }
}