package com.andrei.finalyearprojectapi

import com.andrei.finalyearprojectapi.configuration.BaseIntegrationTest
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.repositories.UserRepository
import com.andrei.finalyearprojectapi.utils.JWTUtils
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value


class JwtTests : BaseIntegrationTest(){
    @Autowired
    protected lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var jwtUtils:JWTUtils

    @Value("\${accessToken.encryptionKey}")
    private lateinit var signingKeyAccessToken:String

    @Value("\${refreshToken.encryptionKey}")
    private lateinit var signingKeyRefreshToken:String

    val user:User = mockk()


    @BeforeEach
    fun setup(){
        coEvery {
            user.email
        } returns "testEmail@gmail.com"

        coEvery {
            user.id
        } returns  10
    }

    @Test
    fun `Given user, access token can be created and signed with key and when decoded with the same key, the signature is valid and the payload can be processed`(){
        val encodedToken = jwtUtils.generateAccessToken(user)
        val payload  = jwtUtils.parseAccessTokenPayload(encodedToken).getOrElse {
            fail("Could not parse token")
        }

        assert(user.id == payload.userID)
    }
    @Test
    fun `Given user, refresh token can be created and signed with key and when decoded with the same key, the signature is valid and the payload can be processed`(){

        val encodedToken = jwtUtils.generateRefreshToken(user)
        val payload = jwtUtils.parseRefreshTokenPayload(encodedToken).getOrElse {
            fail("Could not parse token")
        }
        assert(user.id == payload.userID)
    }

    @Test
    fun `Given expired access token the token is invalid `(){

        val expiredToken = generateExpiredToken(
            user = user,
           signingKey = signingKeyAccessToken
        )

        val parseResult = jwtUtils.parseAccessTokenPayload(expiredToken)

        assert(parseResult.isFailure)
    }

    @Test
    fun `Given expired refresh token the payload returned is null`(){

        val token = generateExpiredToken(
            user = user,
            signingKey = signingKeyRefreshToken
        )
        val parseResult = jwtUtils.parseRefreshTokenPayload(token)

        assert(parseResult.isFailure)
    }



}