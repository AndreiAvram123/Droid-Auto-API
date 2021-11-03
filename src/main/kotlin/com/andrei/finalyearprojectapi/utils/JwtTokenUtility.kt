package com.andrei.finalyearprojectapi.utils

import DecodedJwt
import EncryptedJWTToken
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.entity.enums.UserRole
import com.andrei.finalyearprojectapi.repositories.UserRepository
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
    private val encryptionKeyRefreshToken:String,
    private val userRepository: UserRepository
){

    fun decodeAccessToken(token:String):DecodedJwt = DecodedJwt(token,encryptionKeyAccessToken)
    fun decodeRefreshToken(token:String):DecodedJwt = DecodedJwt(token,encryptionKeyRefreshToken)

    fun generateAccessToken(user: User):EncryptedJWTToken = EncryptedJWTToken(user,encryptionKeyAccessToken, Duration.seconds(durationAccessToken))
    fun generateRefreshToken(user: User):EncryptedJWTToken = EncryptedJWTToken(user,encryptionKeyRefreshToken,Duration.seconds(durationRefreshToken))

    fun getUserRoleFromToken(decodedJwt: DecodedJwt) : UserRole{
        val userID  = decodedJwt.userID ?: return UserRole.UNKNOWN
        val user = userRepository.findTopById(userID) ?: return UserRole.UNKNOWN
        return user.role
    }

}
