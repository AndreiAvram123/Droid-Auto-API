@file:OptIn(ExperimentalTime::class)

package com.andrei.finalyearprojectapi.utils

import DecodedJwt
import EncryptedJWTToken
import com.andrei.finalyearprojectapi.entity.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime


@Component
class JWTTokenUtility(
    @Value("\${accessToken.durationSeconds}")
    private val durationAccessToken:Long,
    @Value(",\${accessToken.encryptionKey}")
    private val encryptionKeyAccessToken:String,
    @Value("\${refreshToken.durationSeconds}")
    private var durationRefreshToken:Long,
    @Value("\${refreshToken.encryptionKey}")
    private val encryptionKeyRefreshToken:String
){

    fun decodeAccessToken(token:String):DecodedJwt = DecodedJwt(token,encryptionKeyAccessToken)

    fun generateAccessToken(user: User):EncryptedJWTToken = EncryptedJWTToken(user,encryptionKeyAccessToken, seconds(durationAccessToken))
    fun generateRefreshToken(user: User):EncryptedJWTToken = EncryptedJWTToken(user,encryptionKeyRefreshToken, seconds(durationRefreshToken))


}
