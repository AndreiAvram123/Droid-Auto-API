
package com.andrei.finalyearprojectapi.utils

import com.andrei.finalyearprojectapi.entity.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import kotlin.time.Duration.Companion.seconds


@Component
class JWTUtils(
    @Value("\${accessToken.durationSeconds}")
    private val durationAccessToken:Long,
    @Value(",\${accessToken.encryptionKey}")
    private val encryptionKeyAccessToken:String,
    @Value("\${refreshToken.durationSeconds}")
    private var durationRefreshToken:Long,
    @Value("\${refreshToken.encryptionKey}")
    private val encryptionKeyRefreshToken:String
){

    fun decodeAccessToken(token:String): DecodedJwt = DecodedJwt(token,encryptionKeyAccessToken)
    fun decodeRefreshToken(token:String):DecodedJwt = DecodedJwt(token,encryptionKeyRefreshToken)


    fun generateAccessToken(user: User): EncodedJWT = EncodedJWT(user,encryptionKeyAccessToken, durationAccessToken.seconds)
    fun generateRefreshToken(user: User): EncodedJWT = EncodedJWT(user,encryptionKeyRefreshToken, durationRefreshToken.seconds)


}
