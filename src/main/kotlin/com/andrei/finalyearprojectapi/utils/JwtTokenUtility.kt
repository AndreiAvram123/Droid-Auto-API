package com.andrei.finalyearprojectapi.utils

import DecodedJwt
import EncryptedJWTToken
import com.andrei.finalyearprojectapi.entity.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Component
class JWTTokenUtility(
    @Value("\${accessToken.durationSeconds}")
    private val durationAccessToken:Long,
    @Value("\${accessToken.encryptionKey}")
    private val encryptionKeyAccessToken:String,
    @Value("\${refreshToken.durationSeconds}")
    private var durationRefreshToken:Long,
    @Value("\${refreshToken.encryptionKey}")
    private val encryptionKeyRefreshToken:String
){

    fun decodeAccessToken(token:String):DecodedJwt = DecodedJwt(token,encryptionKeyAccessToken)
    fun decodeRefreshToken(token:String):DecodedJwt = DecodedJwt(token,encryptionKeyRefreshToken)

    fun generateAccessToken(user: User):EncryptedJWTToken = EncryptedJWTToken(user,encryptionKeyAccessToken, Duration.seconds(durationAccessToken))
    fun generateRefreshToken(user: User):EncryptedJWTToken = EncryptedJWTToken(user,encryptionKeyRefreshToken,Duration.seconds(durationRefreshToken))


}
