
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.entity.enums.UserRole
import com.andrei.finalyearprojectapi.repositories.UserRepository
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import java.util.*
import javax.servlet.http.HttpServletRequest
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

fun HttpServletRequest.getAccessToken():String?{
    return getHeader("Authorization")
}





abstract class JWTToken {
    protected val token_prefix = "Bearer "
    protected val userIDKey = "userID"
}


class DecodedJwt(token: String,
                 private val decryptionKey:String ): JWTToken() {
    private val decodedJWT: DecodedJWT? = decodeToken(token)
    val userID:Long? = decodedJWT?.getClaim(userIDKey)?.asLong()

    fun isValid() = decodedJWT?.subject != null && userID != null


    private fun decodeToken(token:String): DecodedJWT?{
        val verifier = JWT.require(Algorithm.HMAC512(decryptionKey.toByteArray()))
            .build()
        try {
            return verifier.verify(token.replace(token_prefix,""))
        }   catch (e:Exception){
            return null
        }

    }
}

@OptIn(ExperimentalTime::class)
class EncryptedJWTToken constructor(user:User,
                                    private val encryptionKey:String,
                                    private val duration: Duration) : JWTToken() {

    val rawValue = generateTokenForUser(user)
    private fun generateTokenForUser(user: User): String {
        val expirationDate = Date(System.currentTimeMillis() + duration.inWholeMilliseconds)
        return JWT.create().withSubject(user.username)
            .withClaim(userIDKey, user.id)
            .withExpiresAt(expirationDate).sign(Algorithm.HMAC512(encryptionKey.toByteArray()))
    }
}