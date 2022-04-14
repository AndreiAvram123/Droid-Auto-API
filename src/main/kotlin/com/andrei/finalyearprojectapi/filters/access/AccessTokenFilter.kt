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
    private val jwtFactory: JWTFactory
) : SecurityFilter {


    override fun shouldCheckRequest(request:HttpServletRequest):Boolean{
        return !request.endpointHasAnnotation<NoAuthenticationRequired>()
    }


    override fun isFilterPassed(request:HttpServletRequest): Boolean {
        val token = request.getAccessToken() ?: return false
        val decodedToken = jwtFactory.decodeAccessToken(token)

        if (decodedToken.userID != null)
        {
             val user:User = userRepository.findTopById(decodedToken.userID) ?: return false
              filterDataObject.user = user
             return true
        }else{
            return false
        }
    }

    override fun generateErrorResponse(): ResponseWrapper<String>  = notAuthenticated()


}