package com.andrei.finalyearprojectapi

import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.utils.JWTUtils
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class JwtTests {

    @Autowired
    private lateinit var jwtUtils:JWTUtils

    @Value("\${accessToken.encryptionKey}")
    private lateinit var signingKeyAccessToken:String

    @Test
    fun `Given user, access token can be created and signed with key and when decoded with the same key, the signature is valid and the payload can be processed`(){
        val user:User = mockk()
        coEvery {
            user.email
        } returns "testEmail@gmail.com"

        coEvery {
            user.id
        } returns  10

        val encodedToken = jwtUtils.generateAccessToken(user)
        val decodedToken = jwtUtils.parseAccessTokenPayload(encodedToken)
        assert(user.id == decodedToken?.userID)
    }
    @Test
    fun `Given user, refresh token can be created and signed with key and when decoded with the same key, the signature is valid and the payload can be processed`(){
        val user:User = mockk()
        coEvery {
            user.email
        } returns "testEmail@gmail.com"

        coEvery {
            user.id
        } returns  10

        val encodedToken = jwtUtils.generateRefreshToken(user)
        val decodedToken = jwtUtils.parseRefreshTokenPayload(encodedToken)
        assert(user.id == decodedToken?.userID)
    }

    @Test
    fun `Given expired access token the payload returned is null`(){
        val user:User = mockk(relaxed = true)
        every {
            user.email
        } returns "andrei@gmail.com"

        every {
            user.id
        }returns 2

        val pastDate = Date(System.currentTimeMillis() - 10000)

       val expiredToken =  JWT.create().withSubject(user.email)
            .withClaim(JWTUtils.JWTToken.userIDKey, user.id)
            .withExpiresAt(pastDate).sign(Algorithm.HMAC512(signingKeyAccessToken.toByteArray()))

        val parsedPayload = jwtUtils.parseAccessTokenPayload(expiredToken)


        assert(parsedPayload == null )
    }




}