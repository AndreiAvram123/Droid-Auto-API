package com.andrei.finalyearprojectapi.filters.access

import com.andrei.finalyearprojectapi.configuration.annotations.NoAuthenticationRequired
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.filters.FilterDataObject
import com.andrei.finalyearprojectapi.filters.SecurityFilter
import com.andrei.finalyearprojectapi.repositories.UserRepository
import com.andrei.finalyearprojectapi.utils.*
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Component
class AccessTokenFilter(
    private val userRepository: UserRepository,
    private var filterDataObject: FilterDataObject,
    private val jwtUtils: JWTUtils
) : SecurityFilter {


    override fun shouldCheckRequest(request:HttpServletRequest):Boolean{
        return !request.endpointHasAnnotation<NoAuthenticationRequired>()
    }


    override fun isFilterPassed(request:HttpServletRequest): Boolean {
        val token = request.getAccessToken() ?: return false
        val tokenPayload = jwtUtils.parseAccessTokenPayload(token).getOrNull() ?: return false

        val user:User = userRepository.findTopById(tokenPayload.userID) ?: return false
        filterDataObject.setUser(user)
        return true
    }

    override fun generateErrorResponse(): ApiResponse<String>  = notAuthenticated()


}