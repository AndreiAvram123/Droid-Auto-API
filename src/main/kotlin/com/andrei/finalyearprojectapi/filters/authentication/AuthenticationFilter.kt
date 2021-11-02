package com.andrei.finalyearprojectapi.filters.authentication

import EncryptedJWTToken
import com.andrei.finalyearprojectapi.configuration.ApiResponse
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.repositories.UserRepository
import com.andrei.finalyearprojectapi.response.LoginResponse
import com.andrei.finalyearprojectapi.utils.ResponseWrapper
import com.andrei.finalyearprojectapi.utils.okResponse
import com.andrei.finalyearprojectapi.utils.writeJsonResponse
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component
import java.util.stream.Collectors
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

/**
 * Filter used in order to log in a user
 */
@OptIn(ExperimentalTime::class)
@Component
class AuthenticationFilter(
    authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    @Value("\${accessToken.durationSeconds}")
    private val durationAccessToken:Long = 0,
    @Value("\${accessToken.encryptionKey}")
    private val keyAccessToken:String,
    @Value("\${refreshToken.durationSeconds}")
    private var durationRefreshToken:Long = 0,
    @Value("\${refreshToken.encryptionKey}")
    private val keyRefreshToken:String
) : UsernamePasswordAuthenticationFilter(authenticationManager) {



    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse?): Authentication? {
        val requestBody = request.reader.lines().collect(Collectors.joining())
        val gson = Gson()
        val user:User = gson.fromJson(requestBody,User::class.java)!!
        return authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                user.username,
                user.password,
                emptyList()
            )
        )
    }

    override fun successfulAuthentication(request: HttpServletRequest?, response: HttpServletResponse?, chain: FilterChain?, authResult: Authentication?) {

        val userUsername = (authResult?.principal as org.springframework.security.core.userdetails.User).username
        val user = userRepository.findTopByUsername(userUsername)
        user?.let{
            response?.apply {
                writeJsonResponse(
                    okResponse(createLoginResponse(user))
                )
            }
        }
    }

    private fun createLoginResponse(user: User): LoginResponse{
        val accessToken = EncryptedJWTToken(
            user = user,
            duration = Duration.Companion.seconds(durationAccessToken),
            encryptionKey = keyAccessToken
        ).rawValue
        val refreshToken = EncryptedJWTToken(
            user = user,
            duration = Duration.Companion.seconds(durationRefreshToken),
            encryptionKey = keyRefreshToken
        ).rawValue

        return LoginResponse(
            accessToken = accessToken,
            refreshToken = refreshToken
        )

    }


}