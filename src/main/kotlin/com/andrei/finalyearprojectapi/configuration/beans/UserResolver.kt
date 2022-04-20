package com.andrei.finalyearprojectapi.configuration.beans

import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.repositories.UserRepository
import com.andrei.finalyearprojectapi.utils.JWTUtils
import com.andrei.finalyearprojectapi.utils.getAccessToken
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import javax.servlet.http.HttpServletRequest

@Component
class UserResolver(
    private val userRepository: UserRepository,
    private val jwtUtils: JWTUtils
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
       return parameter.parameterType == User::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): User {

        val nativeRequest = webRequest.getNativeRequest(HttpServletRequest::class.java)
        val accessToken = nativeRequest?.getAccessToken() ?: throw Exception()

        val parseResult = jwtUtils.parseAccessTokenPayload(accessToken)

        return userRepository.findTopById(parseResult.getOrThrow().userID) ?: throw Exception()
    }
}