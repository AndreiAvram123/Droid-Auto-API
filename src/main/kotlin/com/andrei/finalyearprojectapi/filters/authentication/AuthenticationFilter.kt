package com.andrei.finalyearprojectapi.filters.authentication


import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.exceptions.InvalidJsonException
import com.andrei.finalyearprojectapi.repositories.UserRepository
import com.andrei.finalyearprojectapi.request.auth.LoginRequest
import com.andrei.finalyearprojectapi.response.LoginResponse
import com.andrei.finalyearprojectapi.utils.JWTUtils
import com.andrei.finalyearprojectapi.utils.okResponse
import com.andrei.finalyearprojectapi.utils.writeJsonResponse
import com.google.gson.Gson
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component
import java.util.stream.Collectors
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Filter used in order to log in a user
 */
@Component
class AuthenticationFilter(
    authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val jwtUtils: JWTUtils
) : UsernamePasswordAuthenticationFilter(authenticationManager) {



    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication? {
        val requestBody = request.reader.lines().collect(Collectors.joining())
        val gson = Gson()
        val loginRequest =  try {
            gson.fromJson(requestBody, LoginRequest::class.java)!!
        }catch (e:Exception){
            throw InvalidJsonException()
        }
        return authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequest.email,
                loginRequest.password,
                emptyList()
            )
        )
    }

    override fun successfulAuthentication(request: HttpServletRequest?, response: HttpServletResponse?, chain: FilterChain?, authResult: Authentication?) {
        val userUsername = (authResult?.principal as org.springframework.security.core.userdetails.User).username
        val user = userRepository.findTopByEmail(userUsername)
        user?.let{
            response?.apply {
                writeJsonResponse(
                    okResponse(createLoginResponse(user))
                )
            }
        }
    }

    private fun createLoginResponse(user: User): LoginResponse{

        val loginResponse = LoginResponse(
            emailVerified = user.emailVerified,
            identityVerified = user.identityVerified
        )
        loginResponse.apply {
            accessToken =   jwtUtils.generateAccessToken(user)
            refreshToken =   jwtUtils.generateRefreshToken(user)
        }
        return  loginResponse;

    }


}