package com.andrei.finalyearprojectapi.filters.access

import DecodedJwt
import com.andrei.finalyearprojectapi.configuration.annotations.AdminTokenRequired
import com.andrei.finalyearprojectapi.entity.enums.UserRole
import com.andrei.finalyearprojectapi.filters.SecurityFilter
import com.andrei.finalyearprojectapi.utils.*

import getAccessToken
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

interface AdminTokenFilter : SecurityFilter

@Component
class AdminTokenFilterImpl(
    private val jwtTokenUtility: JWTTokenUtility
):AdminTokenFilter {

    override fun shouldCheckFilter(request: HttpServletRequest): Boolean  = request.endpointHasAnnotation<AdminTokenRequired>()

    override fun isFilterPassed(request: HttpServletRequest): Boolean {
        val token = request.getAccessToken() ?: return false
        val decodedToken = jwtTokenUtility.decodeAccessToken(token)
        return  jwtTokenUtility.getUserRoleFromToken(decodedToken) == UserRole.ADMIN

    }

    override fun generateErrorResponse(): ResponseWrapper<String>  = notAuthorized()

}
