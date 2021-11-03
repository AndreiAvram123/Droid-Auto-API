package com.andrei.finalyearprojectapi.filters.access

import com.andrei.finalyearprojectapi.configuration.ApiResponse
import com.andrei.finalyearprojectapi.configuration.annotations.NoTokenRequired
import com.andrei.finalyearprojectapi.filters.SecurityFilter
import com.andrei.finalyearprojectapi.utils.JWTTokenUtility
import com.andrei.finalyearprojectapi.utils.ResponseWrapper
import com.andrei.finalyearprojectapi.utils.endpointHasAnnotation
import com.andrei.finalyearprojectapi.utils.notAuthenticated
import getAccessToken
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Component
class AccessTokenFilter(
    private val jwtTokenUtility: JWTTokenUtility
) : SecurityFilter {

    override fun shouldCheckFilter(request:HttpServletRequest):Boolean{
        return !request.endpointHasAnnotation<NoTokenRequired>()
    }

    override fun isFilterPassed(request:HttpServletRequest): Boolean {
        val token = request.getAccessToken() ?: return false
        val decodedToken = jwtTokenUtility.decodeAccessToken(token)
        return decodedToken.isValid()
    }

    override fun generateErrorResponse(): ResponseWrapper<String>  = notAuthenticated()


}