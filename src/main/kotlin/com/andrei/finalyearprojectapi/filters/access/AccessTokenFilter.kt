package com.andrei.finalyearprojectapi.filters.access

import com.andrei.finalyearprojectapi.configuration.annotations.NoTokenRequired
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.filters.SecurityFilter
import com.andrei.finalyearprojectapi.filters.UserDataObject
import com.andrei.finalyearprojectapi.repositories.UserRepository
import com.andrei.finalyearprojectapi.utils.JWTTokenUtility
import com.andrei.finalyearprojectapi.utils.ResponseWrapper
import com.andrei.finalyearprojectapi.utils.endpointHasAnnotation
import com.andrei.finalyearprojectapi.utils.notAuthenticated
import getAccessToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Component
class AccessTokenFilter(
    private val jwtTokenUtility: JWTTokenUtility,
    private val userRepository: UserRepository
) : SecurityFilter {

    @Autowired
    private lateinit var userDataObject: UserDataObject

    override fun shouldCheckRequest(request:HttpServletRequest):Boolean{
        return !request.endpointHasAnnotation<NoTokenRequired>()
    }


    override fun isFilterPassed(request:HttpServletRequest): Boolean {
        val token = request.getAccessToken() ?: return false
        val decodedToken = jwtTokenUtility.decodeAccessToken(token)

        if (decodedToken.isPayloadValid() && decodedToken.userID != null)
        {
             val user:User = userRepository.findTopById(decodedToken.userID) ?: return false
              userDataObject.user = user
             return true
        }else{
            return false
        }
    }

    override fun generateErrorResponse(): ResponseWrapper<String>  = notAuthenticated()


}