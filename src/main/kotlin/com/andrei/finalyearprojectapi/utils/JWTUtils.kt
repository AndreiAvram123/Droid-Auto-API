
package com.andrei.finalyearprojectapi.utils

import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.utils.JWTUtils.JWTToken.Companion.userIDKey
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.HttpServletRequest
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

fun HttpServletRequest.getAccessToken():String?{
    return getHeader(JWTUtils.JWTToken.headerName)?.replace(JWTUtils.JWTToken.tokenPrefix,"")
}
@Component
class JWTUtils(
    @Value("\${accessToken.durationSeconds}")
    private val durationAccessToken:Long,
    @Value("\${accessToken.encryptionKey}")
    private val encryptionKeyAccessToken:String,
    @Value("\${refreshToken.durationSeconds}")
    private var durationRefreshToken:Long,
    @Value("\${refreshToken.encryptionKey}")
    private val encryptionKeyRefreshToken:String
){

    fun parseAccessTokenPayload(token:String): JWTPayload? = parseJwtPayload(token = token, signingKey = encryptionKeyAccessToken)
    fun parseRefreshTokenPayload(token:String):JWTPayload? = parseJwtPayload(token,encryptionKeyRefreshToken)


    fun generateAccessToken(user: User): String = generateSignedToken(user,encryptionKeyAccessToken, durationAccessToken.seconds)
    fun generateRefreshToken(user: User): String = generateSignedToken(user,encryptionKeyRefreshToken, durationRefreshToken.seconds)




    abstract class JWTToken {
        companion object {
            const val tokenPrefix = "Bearer "
            const val headerName = "Authorization"
            const val userIDKey = "userID"
        }
    }



    data class JWTPayload(
        val userID:Long
    )

    private fun generateSignedToken(
        user: User,
        encryptionKey:String,
        duration:Duration

    ): String {
        val expirationDate = Date(System.currentTimeMillis() + duration.inWholeMilliseconds)
        return JWT.create().withSubject(user.email)
            .withClaim(userIDKey, user.id)
            .withExpiresAt(expirationDate).sign(Algorithm.HMAC512(encryptionKey.toByteArray()))
    }

    private fun parseJwtPayload(
        token:String,
        signingKey:String
    ):JWTPayload?{
        val verifier = JWT.require(Algorithm.HMAC512(signingKey.toByteArray())).build()
        return runCatching {
            val decodedJwt =  verifier.verify(token)
            return@runCatching JWTPayload(
                decodedJwt.getClaim(userIDKey)?.asLong() ?: throw Exception("Invalid payload exception")
            )
        }.onFailure {

        }.getOrNull()
    }


}
