package com.andrei.finalyearprojectapi.filters.access

import com.andrei.finalyearprojectapi.configuration.annotations.AdminTokenRequired
import com.andrei.finalyearprojectapi.entity.enums.UserRole
import com.andrei.finalyearprojectapi.filters.SecurityFilter
import com.andrei.finalyearprojectapi.utils.JWTTokenUtility
import com.andrei.finalyearprojectapi.utils.ResponseWrapper
import com.andrei.finalyearprojectapi.utils.endpointHasAnnotation
import com.andrei.finalyearprojectapi.utils.notAuthorized
import getAccessToken
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

abstract class AdminTokenFilter(jwtTokenUtility: JWTTokenUtility) : AccessTokenFilter(jwtTokenUtility)

@Component
class AdminTokenFilterImpl(
    private val jwtTokenUtility: JWTTokenUtility
):AdminTokenFilter(jwtTokenUtility) {

    override fun shouldCheckRequest(request: HttpServletRequest): Boolean  = request.endpointHasAnnotation<AdminTokenRequired>()

    override fun isFilterPassed(request: HttpServletRequest): Boolean {
        val token = request.getAccessToken() ?: return false

        if(!super.isFilterPassed(request)){
            return false
        }

        val decodedToken = jwtTokenUtility.decodeAccessToken(token)
        return  jwtTokenUtility.getUserRoleFromToken(decodedToken) == UserRole.ADMIN

    }

    override fun generateErrorResponse(): ResponseWrapper<String>  = notAuthorized()

}
