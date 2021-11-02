package com.andrei.finalyearprojectapi.filters.access

import com.andrei.finalyearprojectapi.configuration.ApiResponse
import com.andrei.finalyearprojectapi.configuration.annotations.RequireAccessToken
import com.andrei.finalyearprojectapi.filters.SecurityFilter
import com.andrei.finalyearprojectapi.utils.ResponseWrapper
import com.andrei.finalyearprojectapi.utils.endpointHasAnnotation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Component
class AccessTokenFilter : SecurityFilter {

    override fun shouldCheckFilter(request:HttpServletRequest):Boolean{
        return request.endpointHasAnnotation<RequireAccessToken>()
    }

    override fun isFilterPassed(request:HttpServletRequest): Boolean {
       return false
    }

    override fun generateErrorResponse(): ResponseWrapper<String> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.Error(ERROR_UNAUTHORIZED))
    }
    companion object{
        private const val ERROR_UNAUTHORIZED = "Your access token is not valid"
    }

}