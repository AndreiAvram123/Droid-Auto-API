package com.andrei.finalyearprojectapi.filters.authentication

import EncryptedJWTToken
import com.andrei.finalyearprojectapi.configuration.ApiResponse
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.repositories.UserRepository
import com.andrei.finalyearprojectapi.response.LoginResponse
import com.andrei.finalyearprojectapi.utils.ResponseWrapper
import com.andrei.finalyearprojectapi.utils.okResponse
import com.fasterxml.jackson.databind.ObjectMapper
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
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
class AuthenticationFilter(authenticationManager: AuthenticationManager
) : UsernamePasswordAuthenticationFilter(authenticationManager) {


    @Autowired
    private lateinit var userRepository: UserRepository

    @Value("\${accessToken.durationSeconds}")

    private var durationAccessToken:Long = 0
    @Value("\${accessToken.encryptionKey}")

    private lateinit var keyAccessToken:String

    @Value("\${refreshToken.durationSeconds}")
    private var durationRefreshToken:Long = 0

    @Value("\${refreshToken.encryptionKey}")
    private lateinit var keyRefreshToken:String


    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse?): Authentication? {
        val requestBody = request.reader.lines().collect(Collectors.joining())
         val moshi = Moshi.Builder().build()
         val adaptor = moshi.adapter(User::class.java)
        val user:User = adaptor.fromJson(requestBody)!!
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
                writeResponse(
                    createSuccessfulResponse(user)
                )

            }
        }
    }


    private fun createSuccessfulResponse(user: User): ResponseWrapper<LoginResponse>{
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
        return okResponse(
            LoginResponse(
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        )
    }

    /**
     * Helper method to write a json object into
     * the servlet response
     */
    private fun HttpServletResponse.writeResponse(response:ResponseWrapper<LoginResponse>){
        val moshi = Moshi.Builder().build()
        val adapter =  moshi.adapter(ApiResponse::class.java)
        this.writer.write(adapter.toJson(response.body))
        this.contentType = MediaType.APPLICATION_JSON_VALUE
    }

}