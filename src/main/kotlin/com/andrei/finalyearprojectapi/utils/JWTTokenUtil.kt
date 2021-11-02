
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.entity.enums.UserRole
import com.andrei.finalyearprojectapi.repositories.UserRepository
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT

import java.util.*
import javax.servlet.http.HttpServletRequest
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.days
import kotlin.time.minutes

fun HttpServletRequest.getAccessToken():String?{
    return getHeader("Authorization")
}

abstract class JWTToken {
    protected val TOKEN_PREFIX = "Bearer "
    protected val userIDKey = "userID"
    protected val userRoleKey = "userRole"
}


class DecryptedJWTToken(token: String,
                        private val decryptionKey:String ): JWTToken() {
    val decodedJWT:DecodedJWT? = decodeToken(token)
    val username:String? = decodedJWT?.subject
    val userID:Long? = decodedJWT?.getClaim(userIDKey)?.asLong()

    fun isAdminToken(userRepository: UserRepository):Boolean {
        when {
            userID == null -> {
                return false
            }
            userRepository.findTopById(userID) == null -> {
                return false
            }
            else -> {
                userRepository.findTopById(userID)?.let {
                    if(it.role == UserRole.ADMIN){
                        return true
                    }
                }
            }
        }
        return false
    }
    private fun decodeToken(token:String):DecodedJWT?{
        val verifier = JWT.require(Algorithm.HMAC512(decryptionKey.toByteArray()))
            .build()
        try {
            return verifier.verify(token.replace(TOKEN_PREFIX,""))
        }   catch (e:Exception){
            return null
        }

    }
}
@OptIn(ExperimentalTime::class)
class EncryptedJWTToken(user:User,
                        private val encryptionKey:String,
                        private val duration: Duration) : JWTToken() {

    val rawValue = generateTokenForUser(user)
    private fun generateTokenForUser(user: User): String {
        val expirationDate = Date(System.currentTimeMillis() + duration.inWholeMilliseconds)
        return JWT.create().withSubject(user.username)
            .withClaim(userIDKey, user.id)
            .withClaim(userRoleKey, user.role.value)
            .withExpiresAt(expirationDate).sign(Algorithm.HMAC512(encryptionKey.toByteArray()))
    }
}